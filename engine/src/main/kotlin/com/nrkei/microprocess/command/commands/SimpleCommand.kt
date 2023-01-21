/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.commands

import com.nrkei.microprocess.command.commands.ExecutionResult.*
import com.nrkei.microprocess.command.commands.TaskResult.*

// Understands the execution of a desired operation
class SimpleCommand internal constructor(private val executionTask: Task, private val undoTask: Task) : Command {
    private var state: State = NotExecuted

    override fun execute() = state.execute(this)

    override fun undo() = state.undo(this)

    private fun executeTask() =
        try {
            executionTask.execute().also { result ->
                state = when (result) {
                    TASK_SUCCEEDED -> Successful
                    TASK_FAILED -> Failed
                    TASK_SUSPENDED -> Suspended
                }
            }
            state.commandResult
        } catch (e: Exception) {
            state = Failed
            FAILED
        }

    private fun undoTask() =
        try {
            state = when (undoTask.execute()) {
                TASK_SUCCEEDED -> Reversed
                TASK_FAILED -> ReversalFailed
                TASK_SUSPENDED -> ReversalFailed  // Maybe support this?
            }
            state.commandResult
        } catch (e: Exception) {
            state = ReversalFailed
            REVERSAL_FAILED
        }

    override fun accept(visitor: CommandVisitor) {
        visitor.visit(this, state.commandResult, executionTask)
    }

    interface State {
        val commandResult: ExecutionResult
        fun execute(command: SimpleCommand): ExecutionResult
        fun undo(command: SimpleCommand): ExecutionResult = REVERSED
    }

    object NotExecuted : State {
        override val commandResult = NOT_EXECUTED
        override fun execute(command: SimpleCommand) = command.executeTask()
    }

    object Successful : State {
        override val commandResult = SUCCEEDED
        override fun execute(command: SimpleCommand) = SUCCEEDED
        override fun undo(command: SimpleCommand) = command.undoTask()
    }

    object Failed : State {
        override val commandResult = FAILED
        override fun execute(command: SimpleCommand) = FAILED
    }

    object Suspended : State {
        override val commandResult = SUSPENDED
        override fun execute(command: SimpleCommand) = command.executeTask()
        override fun undo(command: SimpleCommand) =
            throw IllegalStateException("Attempting to undo a Suspended command")
    }

    object Reversed : State {
        override val commandResult = REVERSED
        override fun execute(command: SimpleCommand) = FAILED
    }

    object ReversalFailed : State {
        override val commandResult = REVERSAL_FAILED
        override fun execute(command: SimpleCommand) = FAILED
        override fun undo(command: SimpleCommand) = REVERSAL_FAILED
    }
}