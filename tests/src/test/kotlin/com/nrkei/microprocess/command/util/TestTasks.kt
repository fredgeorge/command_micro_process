/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.util

import com.nrkei.microprocess.command.ExecutionStatus
import com.nrkei.microprocess.command.Task

internal class TestTask(private val status: ExecutionStatus) : Task {
    override fun execute() = status
}

internal object CrashingTask : Task {
    override fun execute(): ExecutionStatus {
        throw IllegalArgumentException("deliberate crash")
    }

}