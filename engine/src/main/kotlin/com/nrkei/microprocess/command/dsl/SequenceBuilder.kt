/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.dsl

import com.nrkei.microprocess.command.commands.Command
import com.nrkei.microprocess.command.commands.SequenceCommand
import com.nrkei.microprocess.command.commands.SimpleCommand
import com.nrkei.microprocess.command.commands.Task

fun sequence(block: SequenceBuilder.() -> Unit) =
    SequenceBuilder()
        .also { it.block() }.result()

class SequenceBuilder {
    private val commands = mutableListOf<Command>()
    private lateinit var executionTask: Task

    internal fun result() = SequenceCommand(commands)

    val first get() = this

    val next get() = this

    infix fun perform(executionLabel: TaskLabel) = this.also{
        executionTask = executionLabel.task()
    }

    infix fun otherwise(undoLabel: TaskLabel) {
        commands.add(SimpleCommand(executionTask))
    }
}