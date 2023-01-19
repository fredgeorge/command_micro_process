/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.commands

import com.nrkei.microprocess.command.commands.ExecutionResult.*

// Understands the execution of a single Task
class SimpleCommand internal constructor(private val task: Task) : Command {
    private var state: State = Initial

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
        visitor.visit(this, task)
    }

    private interface State {
        fun execute(command: SimpleCommand): ExecutionResult
    }

    object Initial: State {
        override fun execute(command: SimpleCommand) = command.executeTask()
    }

    object Successful: State {
        override fun execute(command: SimpleCommand) = SUCCEEDED
    }

    object Failed: State {
        override fun execute(command: SimpleCommand) = FAILED
    }

    object Suspended: State {
        override fun execute(command: SimpleCommand) = command.executeTask()
    }
}