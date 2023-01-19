/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.util

import com.nrkei.microprocess.command.commands.Command
import com.nrkei.microprocess.command.commands.CommandVisitor
import com.nrkei.microprocess.command.commands.SimpleCommand
import com.nrkei.microprocess.command.commands.SimpleCommand.CommandState
import com.nrkei.microprocess.command.commands.Task

internal class TestAnalysis(command: Command) : CommandVisitor {
    internal val tasks = mutableMapOf<CommandState, MutableList<Task>>()
    init {
        command.accept(this)
    }

    override fun visit(command: SimpleCommand, state: CommandState, executionTask: Task) {
        tasks.putIfAbsent(state, mutableListOf())
        tasks[state]?.add(executionTask)
    }

    internal operator fun get(state: CommandState): List<Task> {
        tasks.putIfAbsent(state, mutableListOf())
        return tasks[state]!!
    }
}