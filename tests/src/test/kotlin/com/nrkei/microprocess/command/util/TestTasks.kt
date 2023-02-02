/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.util

import com.nrkei.microprocess.command.commands.Context
import com.nrkei.microprocess.command.commands.Task
import com.nrkei.microprocess.command.commands.TaskResult
import com.nrkei.microprocess.command.commands.TaskResult.*
import com.nrkei.microprocess.command.dsl.TaskLabel

internal class TestTask(private val status: TaskResult) : Task {
    internal var executionCount = 0
    override fun execute(c: Context) = status.also { executionCount += 1 }
}

internal object CrashingTask : Task {
    internal var executionCount = 0
    override fun execute(c: Context): Nothing {
        executionCount += 1
        throw IllegalArgumentException("deliberate crash")
    }
}

internal enum class TestLabel(private val taskGenerator: () -> Task): TaskLabel {
    SUCCESSFUL_TASK({ TestTask(TASK_SUCCEEDED) }),
    SUCCESSFUL_RECOVERY({ TestTask(TASK_SUCCEEDED) }),
    FAILED_TASK({ TestTask(TASK_FAILED) }),
    FAILED_RECOVERY({ TestTask(TASK_FAILED) }),
    SUSPENDED_TASK({ TestTask(TASK_SUSPENDED) }),
    CRASHED_TASK({ CrashingTask });

    override fun task() = taskGenerator()
}
