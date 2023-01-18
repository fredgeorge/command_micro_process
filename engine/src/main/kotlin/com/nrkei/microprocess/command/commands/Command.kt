/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.commands

// Understands something that can be done (and undone)
interface Command {
    fun execute(): ExecutionStatus
}

interface Task {
    fun execute(): ExecutionStatus
}

// Understands the results of Command Execution
enum class ExecutionStatus {
    SUCCEEDED, FAILED, SUSPENDED
}