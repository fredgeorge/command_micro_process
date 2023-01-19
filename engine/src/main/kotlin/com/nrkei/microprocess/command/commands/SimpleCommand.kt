/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.commands

import com.nrkei.microprocess.command.commands.ExecutionResult.*
import com.nrkei.microprocess.command.commands.SimpleCommand.CommandState.NOT_EXECUTED
import com.nrkei.microprocess.command.commands.SimpleCommand.CommandState.SUCCESSFUL

// Understands the execution of a single Task
class SimpleCommand internal constructor(private val task: Task) : Command {
    private var state: State = NotExecuted

    override fun execute() = state.execute(this)

    private fun executeTask() =
        try {
            task.execute().also { result ->
                state = when (result) {
                    SUCCEEDED -> Successful
                    FAILED -> Failed
                    SUSPENDED -> Suspended
                }
            }
        } catch (e: Exception) {
            state = Failed
            FAILED
        }

    override fun accept(visitor: CommandVisitor) {
        visitor.visit(this, state.commandState, task)
    }

    enum class CommandState {
        NOT_EXECUTED, SUCCESSFUL, FAILED, SUSPENDED
    }

    interface State {
        val commandState: CommandState
        fun execute(command: SimpleCommand): ExecutionResult
    }

    object NotExecuted: State {
        override val commandState = NOT_EXECUTED
        override fun execute(command: SimpleCommand) = command.executeTask()
    }

    object Successful: State {
        override val commandState = SUCCESSFUL
        override fun execute(command: SimpleCommand) = SUCCEEDED
    }

    object Failed: State {
        override val commandState = CommandState.FAILED

        override fun execute(command: SimpleCommand) = FAILED
    }

    object Suspended: State {
        override val commandState = CommandState.SUSPENDED
        override fun execute(command: SimpleCommand) = command.executeTask()
    }
}