/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command

import com.nrkei.microprocess.command.commands.CommandVisitor
import com.nrkei.microprocess.command.commands.SequenceCommand

class CommandMap(command: SequenceCommand) : CommandVisitor {
    private var state: State = Process
    private lateinit var currentCommand: SequenceCommand
    init {
        command.accept(this)
    }

    override fun preVisit(command: SequenceCommand, subCommandCount: Int) {
        state.preVisit(this, command, subCommandCount)
    }

    override fun postVisit(command: SequenceCommand) {
        state.postVisit(this, command)
    }

    private interface State {
        fun preVisit(commandMap: CommandMap, command: SequenceCommand, subCommandCount: Int)
        fun postVisit(commandMap: CommandMap, command: SequenceCommand)
    }

    internal object Process: State {
        override fun preVisit(commandMap: CommandMap, command: SequenceCommand, subCommandCount: Int) {
            commandMap.currentCommand = command
            commandMap.state = Ignore
        }

        override fun postVisit(commandMap: CommandMap, command: SequenceCommand) {
            TODO("Not yet implemented")
        }
    }

    internal object Ignore: State {
        override fun preVisit(commandMap: CommandMap, command: SequenceCommand, subCommandCount: Int) {
            // Ignore
        }

        override fun postVisit(commandMap: CommandMap, command: SequenceCommand) {
            if(commandMap.currentCommand != command) return
            commandMap.state = Process
        }
    }
}