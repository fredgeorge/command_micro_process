/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.util

import com.nrkei.microprocess.command.commands.ExecutionStatus
import com.nrkei.microprocess.command.commands.ExecutionStatus.SUCCEEDED
import com.nrkei.microprocess.command.commands.Task
import com.nrkei.microprocess.command.dsl.TaskLabel

internal class TestTask(private val status: ExecutionStatus) : Task {
    override fun execute() = status
}

internal object CrashingTask : Task {
    override fun execute() = throw IllegalArgumentException("deliberate crash")
}

internal enum class TestLabel(private val task: Task): TaskLabel {
    SUCCESSFUL_TASK(TestTask(SUCCEEDED)),
    SUCCESSFUL_RECOVERY(TestTask(SUCCEEDED));

    override fun task() = task
}
