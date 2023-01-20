/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.util

import com.nrkei.microprocess.command.commands.*

internal class TestAnalysis(command: Command) : CommandVisitor {
    internal val tasks = mutableMapOf<ExecutionResult, MutableList<Task>>()
    init {
        command.accept(this)
    }

    override fun visit(command: SimpleCommand, state: ExecutionResult, executionTask: Task) {
        tasks.putIfAbsent(state, mutableListOf())
        tasks[state]?.add(executionTask)
    }

    internal operator fun get(state: ExecutionResult): List<Task> {
        tasks.putIfAbsent(state, mutableListOf())
        return tasks[state]!!
    }
}