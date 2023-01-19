/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.unit

import com.nrkei.microprocess.command.commands.ExecutionResult.*
import com.nrkei.microprocess.command.dsl.TaskLabel
import com.nrkei.microprocess.command.dsl.sequence
import com.nrkei.microprocess.command.util.TestAnalysis
import com.nrkei.microprocess.command.util.TestLabel.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

// Ensures SimpleCommands operate correctly
internal class SimpleCommandTest {

    @Test fun success() {
        assertEquals(SUCCEEDED, simpleCommand(SUCCESSFUL_TASK).execute())
    }

    @Test fun failure() {
        assertEquals(FAILED, simpleCommand(FAILED_TASK).execute())
    }

    @Test fun suspension() {
        assertEquals(SUSPENDED, simpleCommand(SUSPENDED_TASK).execute())
    }

    @Test fun crashed() {
        assertEquals(FAILED, simpleCommand(CRASHED_TASK).execute())
    }

    @Test fun `executes only once`() {
        simpleCommand(SUCCESSFUL_TASK).also { command ->
            assertEquals(SUCCEEDED, command.execute())
            assertEquals(1, TestAnalysis(command).successfulCount)
            assertEquals(SUCCEEDED, command.execute())
            assertEquals(1, TestAnalysis(command).successfulCount)
        }
    }

    private fun simpleCommand(executionLabel: TaskLabel) =
        sequence {
            first perform executionLabel otherwise executionLabel
        }
}
