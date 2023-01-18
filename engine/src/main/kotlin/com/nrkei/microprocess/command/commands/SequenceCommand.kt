/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.commands

import com.nrkei.microprocess.command.commands.ExecutionStatus.*

// Understands a process involving multiple steps executed in order
class SequenceCommand(private val commands: List<Command>) : Command {

    override fun execute(): ExecutionStatus {
        return execute(commands.toMutableList())
    }

    private fun execute(commands: MutableList<Command>): ExecutionStatus {
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