/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.util

import com.nrkei.microprocess.command.Command
import com.nrkei.microprocess.command.ExecutionStatus

internal class TestCommand(private val status: ExecutionStatus) : Command {
    override fun execute() = status
}