/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.commands

interface CommandVisitor {
    fun preVisit(command: SequenceCommand, subCommandCount: Int) { }
    fun visit(command: SimpleCommand, executionTask: Task) { }
    fun postVisit(command: SequenceCommand) { }
}