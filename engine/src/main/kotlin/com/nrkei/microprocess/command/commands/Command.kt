/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.commands

// Understands something that can be done (and undone)
interface Command {
    fun execute(c: Context): ExecutionResult
    fun undo(c: Context): ExecutionResult
    fun accept(visitor: CommandVisitor)
}

// Understands the outcome of Command Execution
enum class ExecutionResult {
    NOT_EXECUTED, SUCCEEDED, FAILED, SUSPENDED, REVERSED, REVERSAL_FAILED
}

// Understands a specific unit of work
interface Task {
    val referencedLabels get() = emptyList<ParameterLabel>()
    val updatedLabels get() = emptyList<ParameterLabel>()
    fun execute(c: Context): TaskResult
}

// Understands the outcome of Command Execution
enum class TaskResult {
    TASK_SUCCEEDED, TASK_FAILED, TASK_SUSPENDED
}