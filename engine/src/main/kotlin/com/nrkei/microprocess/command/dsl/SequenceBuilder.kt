/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.dsl

import com.nrkei.microprocess.command.commands.*
import com.nrkei.microprocess.command.commands.TaskResult.TASK_FAILED
import com.nrkei.microprocess.command.commands.TaskResult.TASK_SUCCEEDED

fun sequence(block: SequenceBuilder.() -> Unit) : SequenceCommand =
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

    infix fun perform(sequence: SequenceCommand) {
        commands.add(sequence)
    }

    infix fun otherwise(undoLabel: TaskLabel) {
        commands.add(SimpleCommand(executionTask, undoLabel.task()))
    }

    infix fun reversal(taskId: DefaultTask) {
        commands.add(SimpleCommand(executionTask, taskId.task))
    }

    enum class DefaultTask(internal val task: Task) {
        unnecessary( object: Task { override fun execute(c: Context) = TASK_SUCCEEDED }),
        impossible( object: Task { override fun execute(c: Context) = TASK_FAILED })
    }
}