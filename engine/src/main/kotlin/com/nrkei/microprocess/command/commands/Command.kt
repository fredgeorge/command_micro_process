/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.commands

// Understands something that can be done (and undone)
interface Command {
    fun execute(): ExecutionResult
    fun accept(visitor: CommandVisitor)
}

interface Task {
    fun execute(): ExecutionResult
}

// Understands the outcome of Command Execution
enum class ExecutionResult {
    SUCCEEDED, FAILED, SUSPENDED
}