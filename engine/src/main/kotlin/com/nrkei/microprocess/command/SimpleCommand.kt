/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command

// Understands the execution of a single Task
class SimpleCommand(private val task: Task) : Command {
    override fun execute() = task.execute()
}