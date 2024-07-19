/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.commands

import com.nrkei.microprocess.command.commands.ExecutionResult.*

// Understands a process involving multiple steps executed in order
class SequenceCommand internal constructor(private val commands: List<Command>) : Command {

    override fun execute(c: Context): ExecutionResult {
        return execute(c, commands.toMutableList())
    }

    // Recursion: Process first command, then recurse with remaining
    private fun execute(c: Context, commands: MutableList<Command>): ExecutionResult {
        if (commands.isEmpty()) return SUCCEEDED
        commands.removeAt(0).also { currentCommand ->
            return when (currentCommand.execute(c)) {
                NOT_EXECUTED -> throw IllegalStateException("Invalid execution result of NOT_EXECUTED for current command")
                SUCCEEDED -> {
                    when (execute(c, commands)) {
                        NOT_EXECUTED -> throw IllegalStateException("Invalid execution result of NOT_EXECUTED for children")
                        SUCCEEDED -> SUCCEEDED
                        FAILED -> currentCommand.undo(c)
                        SUSPENDED -> SUSPENDED
                        REVERSED -> currentCommand.undo(c)
                        REVERSAL_FAILED -> REVERSAL_FAILED.also { currentCommand.undo(c) }
                    }
                }
                FAILED -> FAILED
                SUSPENDED -> SUSPENDED
                REVERSED -> REVERSED
                // SequenceCommand fails reversal if any subcommand reversal fails
                REVERSAL_FAILED -> REVERSAL_FAILED
            }
        }
    }

    override fun undo(c: Context) =
        if (commands.reversed().map { it.undo(c) }.all { it == REVERSED }) REVERSED
        else REVERSAL_FAILED

    override fun accept(visitor: CommandVisitor) {
        visitor.preVisit(this, commands.size)
        commands.forEach { it.accept(visitor) }
        visitor.postVisit(this)
    }
}