/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.commands

import com.nrkei.microprocess.command.commands.ExecutionResult.*

// Understands a process involving multiple steps executed in order
internal class SequenceCommand(private val commands: List<Command>) : Command {

    override fun execute(): ExecutionResult {
        return execute(commands.toMutableList())
    }

    private fun execute(commands: MutableList<Command>): ExecutionResult {
        if (commands.isEmpty()) return SUCCEEDED
        commands.removeAt(0).also { currentCommand ->
            return when(currentCommand.execute()) {
                SUCCEEDED -> execute(commands)
                FAILED -> FAILED
                SUSPENDED -> SUSPENDED
            }
        }
    }
}