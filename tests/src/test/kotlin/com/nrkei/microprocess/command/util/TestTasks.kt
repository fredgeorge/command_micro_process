/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.util

import com.nrkei.microprocess.command.commands.ExecutionResult
import com.nrkei.microprocess.command.commands.ExecutionResult.*
import com.nrkei.microprocess.command.commands.Task
import com.nrkei.microprocess.command.dsl.TaskLabel

internal class TestTask(private val status: ExecutionResult) : Task {
    internal var executionCount = 0
    override fun execute() = status.also { executionCount += 1 }
}

internal object CrashingTask : Task {
    internal var executionCount = 0
    override fun execute(): Nothing {
        executionCount += 1
        throw IllegalArgumentException("deliberate crash")
    }
}

internal enum class TestLabel(private val taskGenerator: () -> Task): TaskLabel {
    SUCCESSFUL_TASK({ TestTask(SUCCEEDED) }),
    SUCCESSFUL_RECOVERY({ TestTask(SUCCEEDED) }),
    FAILED_TASK({ TestTask(FAILED) }),
    FAILED_RECOVERY({ TestTask(FAILED) }),
    SUSPENDED_TASK({ TestTask(SUSPENDED) }),
    CRASHED_TASK({ CrashingTask });

    override fun task() = taskGenerator()
}
