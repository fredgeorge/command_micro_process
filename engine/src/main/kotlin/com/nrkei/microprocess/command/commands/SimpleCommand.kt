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

    override fun execute(c: Context) = state.execute(c, this)

    override fun undo(c: Context) = state.undo(c, this)

    private fun executeTask(c: Context) =
        try {
            executionTask.execute(c).also { result ->
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

    private fun undoTask(c: Context) =
        try {
            state = when (undoTask.execute(c)) {
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
        fun execute(c: Context, command: SimpleCommand): ExecutionResult
        fun undo(c: Context, command: SimpleCommand): ExecutionResult = REVERSED
    }

    object NotExecuted : State {
        override val commandResult = NOT_EXECUTED
        override fun execute(c: Context, command: SimpleCommand) = command.executeTask(c)
    }

    object Successful : State {
        override val commandResult = SUCCEEDED
        override fun execute(c: Context, command: SimpleCommand) = SUCCEEDED
        override fun undo(c: Context, command: SimpleCommand) = command.undoTask(c)
    }

    object Failed : State {
        override val commandResult = FAILED
        override fun execute(c: Context, command: SimpleCommand) = FAILED
    }

    object Suspended : State {
        override val commandResult = SUSPENDED
        override fun execute(c: Context, command: SimpleCommand) = command.executeTask(c)
        override fun undo(c: Context, command: SimpleCommand) =
            throw IllegalStateException("Attempting to undo a Suspended command")
    }

    object Reversed : State {
        override val commandResult = REVERSED
        override fun execute(c: Context, command: SimpleCommand) = FAILED
    }

    object ReversalFailed : State {
        override val commandResult = REVERSAL_FAILED
        override fun execute(c: Context, command: SimpleCommand) = FAILED
        override fun undo(c: Context, command: SimpleCommand) = REVERSAL_FAILED
    }
}