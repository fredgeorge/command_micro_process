/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.unit

import com.nrkei.microprocess.command.commands.Context
import com.nrkei.microprocess.command.commands.ExecutionResult.*
import com.nrkei.microprocess.command.dsl.TaskLabel
import com.nrkei.microprocess.command.dsl.sequence
import com.nrkei.microprocess.command.util.TestTaskLabel.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

// Ensures SimpleCommands operate correctly
internal class SimpleCommandTest {
    private lateinit var c: Context

    @BeforeEach fun setup() {
        c = Context()
    }

    @Test fun success() {
        assertEquals(SUCCEEDED, simpleCommand(SUCCESSFUL_TASK).execute(c))
    }

    @Test fun failure() {
        assertEquals(FAILED, simpleCommand(FAILED_TASK).execute(c))
    }

    @Test fun suspension() {
        assertEquals(SUSPENDED, simpleCommand(SUSPENDED_TASK).execute(c))
    }

    @Test fun crashed() {
        assertEquals(FAILED, simpleCommand(CRASHED_TASK).execute(c))
    }

    private fun simpleCommand(executionLabel: TaskLabel) =
        sequence {
            first perform executionLabel otherwise executionLabel
        }
}
