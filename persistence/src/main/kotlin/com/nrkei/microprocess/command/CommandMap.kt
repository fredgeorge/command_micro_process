/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command

import com.nrkei.microprocess.command.commands.*

class CommandMap(private val command: SequenceCommand) : CommandVisitor {
    private var state: State = Process
    private lateinit var subCommand: SequenceCommand
    private val subMaps = mutableListOf<Map<String, Any>>()

    fun result(): Map<String, Any> {
        command.accept(this)
        return listOf(
            "type" to command.javaClass.simpleName,
            "children" to subMaps
        ).toMap()
    }

    override fun preVisit(command: SequenceCommand, subCommandCount: Int) {
        state.preVisit(this, command, subCommandCount)
    }

    override fun postVisit(command: SequenceCommand) {
        state.postVisit(this, command)
    }

    override fun visit(command: SimpleCommand, state: ExecutionResult, executionTask: Task, undoTask: Task) {
        this.state.visit(this, command, state, executionTask, undoTask)
    }

    private interface State {
        fun preVisit(commandMap: CommandMap, command: SequenceCommand, subCommandCount: Int)
        fun postVisit(commandMap: CommandMap, command: SequenceCommand)
        fun visit(
            commandMap: CommandMap,
            command: SimpleCommand,
            state: ExecutionResult,
            executionTask: Task,
            undoTask: Task
        )
    }

    internal object Process: State {
        override fun preVisit(commandMap: CommandMap, command: SequenceCommand, subCommandCount: Int) {
            if (command != commandMap.command){
                commandMap.subCommand = command
                commandMap.state = Ignore
                commandMap.subMaps.add(CommandMap(command).result())
            }
        }

        override fun postVisit(commandMap: CommandMap, command: SequenceCommand) {
            // Nothing to do here
        }

        override fun visit(
            commandMap: CommandMap,
            command: SimpleCommand,
            state: ExecutionResult,
            executionTask: Task,
            undoTask: Task
        ) {
            commandMap.subMaps.add(listOf(
                "type" to command.javaClass.simpleName,
                "state" to state.name,
                "executionTask" to executionTask.javaClass.simpleName,
                "undoTask" to undoTask.javaClass.simpleName,
            ).toMap())
        }
    }

    internal object Ignore: State {
        override fun preVisit(commandMap: CommandMap, command: SequenceCommand, subCommandCount: Int) {
            // Ignore
        }

        override fun postVisit(commandMap: CommandMap, command: SequenceCommand) {
            if(commandMap.subCommand != command) return
            commandMap.state = Process
        }

        override fun visit(
            commandMap: CommandMap,
            command: SimpleCommand,
            state: ExecutionResult,
            executionTask: Task,
            undoTask: Task
        ) {
            // Ignore - handled in recursive invocation
        }
    }
}