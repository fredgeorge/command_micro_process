/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.unit

import com.nrkei.microprocess.command.commands.Context
import com.nrkei.microprocess.command.commands.ExecutionResult.SUCCEEDED
import com.nrkei.microprocess.command.dsl.TaskLabel
import com.nrkei.microprocess.command.util.TestParameterLabel
import com.nrkei.microprocess.command.util.TestParameterLabel.*
import com.nrkei.microprocess.command.util.TestTaskLabel.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class TaskContextTest {
    private lateinit var c: Context

    @BeforeEach fun setup() {
        c = Context()
        listOf(A, B, C).forEach { label -> c[label] = label.name }
    }

    @Test fun `read A, B, C write D`() {
        assertEquals(SUCCEEDED, simpleCommand(READ_ABC_WRITE_D).execute(c))
        assertParameters("A", "B", "C", "D+")
    }

    @Test fun `read A, B, C write B, D`() {
        assertEquals(SUCCEEDED, simpleCommand(READ_ABC_WRITE_BD).execute(c))
        assertParameters("A", "B+", "C", "D+")
    }

    private fun simpleCommand(executionLabel: TaskLabel) =
        com.nrkei.microprocess.command.dsl.sequence {
            first perform executionLabel otherwise executionLabel
        }

    private fun assertParameters(vararg values: String) {
        // Check expected values
        TestParameterLabel.values().zip(values.toList()).toMap().forEach { label, value ->
            assertEquals(value, c string label)
        }
        // Ensure other values not set
        TestParameterLabel.values().takeLast(TestParameterLabel.values().size - values.size).forEach { label ->
            assertThrows<IllegalArgumentException> { c string label }
        }
    }
}
