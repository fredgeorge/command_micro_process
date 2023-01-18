/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command

interface Command {
    fun execute(): ExecutionStatus
}

enum class ExecutionStatus {
    SUCCEEDED, FAILED, SUSPENDED
}