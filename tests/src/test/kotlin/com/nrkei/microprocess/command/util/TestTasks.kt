/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.util

import com.nrkei.microprocess.command.commands.Context
import com.nrkei.microprocess.command.commands.ParameterLabel
import com.nrkei.microprocess.command.commands.Task
import com.nrkei.microprocess.command.commands.TaskResult
import com.nrkei.microprocess.command.commands.TaskResult.*
import com.nrkei.microprocess.command.dsl.TaskLabel
import org.junit.jupiter.api.Assertions.assertEquals

internal class TestTask(
    private val status: TaskResult,
    override val referencedLabels: List<ParameterLabel> = emptyList(),
    override val updatedLabels: List<ParameterLabel> = emptyList()
) : Task {
    override fun execute(c: Context) = status.also {
        referencedLabels.forEach { label -> assertEquals(label.name, c string label) }
        updatedLabels.forEach { label -> c[label] = label.name }
    }
}

internal object CrashingTask : Task {
    override fun execute(c: Context): Nothing {
        throw IllegalArgumentException("deliberate crash")
    }
}

internal enum class TestTaskLabel(private val taskGenerator: () -> Task): TaskLabel {
    SUCCESSFUL_TASK({ TestTask(TASK_SUCCEEDED) }),
    SUCCESSFUL_RECOVERY({ TestTask(TASK_SUCCEEDED) }),
    FAILED_TASK({ TestTask(TASK_FAILED) }),
    FAILED_RECOVERY({ TestTask(TASK_FAILED) }),
    SUSPENDED_TASK({ TestTask(TASK_SUSPENDED) }),
    CRASHED_TASK({ CrashingTask });

    override fun task() = taskGenerator()
}

internal enum class TestParameterLabel: ParameterLabel {
    A, B, C, D, E
}
