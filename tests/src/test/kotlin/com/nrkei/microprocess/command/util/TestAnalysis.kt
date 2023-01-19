/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.util

import com.nrkei.microprocess.command.commands.Command
import com.nrkei.microprocess.command.commands.CommandVisitor
import com.nrkei.microprocess.command.commands.SimpleCommand
import com.nrkei.microprocess.command.commands.Task

internal class TestAnalysis(command: Command) : CommandVisitor {
    internal var successfulCount = 0
    init {
        command.accept(this)
    }

    override fun visit(command: SimpleCommand, executionTask: Task) {
        successfulCount += (executionTask as TestTask).executionCount
    }
}