/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.commands

import com.nrkei.microprocess.command.commands.ExecutionResult.*

// Understands a process involving multiple steps executed in order
class SequenceCommand internal constructor(private val commands: List<Command>) : Command {

    override fun execute(): ExecutionResult {
        return execute(commands.toMutableList())
    }

    // Recursion: Process first command, then recurse with remaining
    private fun execute(commands: MutableList<Command>): ExecutionResult {
        if (commands.isEmpty()) return SUCCEEDED
        return when (commands.removeAt(0).execute()) {
            SUCCEEDED -> execute(commands)
            FAILED -> FAILED
            SUSPENDED -> SUSPENDED
        }
    }

    override fun accept(visitor: CommandVisitor) {
        visitor.preVisit(this, commands.size)
        commands.forEach { it.accept(visitor) }
        visitor.postVisit(this)
    }
}