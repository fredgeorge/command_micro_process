/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.unit

import com.nrkei.microprocess.command.commands.Context
import com.nrkei.microprocess.command.commands.ParameterLabel
import com.nrkei.microprocess.command.unit.ContextTest.DataLabel.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.ClassCastException

internal class ContextTest {
    private lateinit var c: Context

    private enum class DataLabel : ParameterLabel {
        AGE, WEALTH, NAME, SPOUSE
    }

    @BeforeEach fun setup() {
        c = Context()
        c[AGE] = 45
        c[NAME] = "Jennifer"
        c[WEALTH] = 0.45e6
    }

    @Test fun access() {
        assertEquals(45, c int AGE)
        assertEquals("Jennifer", c string NAME)
        assertEquals(450_000.0, c double WEALTH)
        assertThrows<IllegalArgumentException> { c string SPOUSE }
        assertThrows<ClassCastException> { c double AGE }
        assertThrows<ClassCastException> { c int NAME }
        c[SPOUSE] = "Harold"
        assertEquals("Harold", c string SPOUSE)
        assertThrows<ClassCastException> { c double SPOUSE }
    }

    @Test fun `extract sub-context`() {
        c.subset(AGE, NAME).also { subContext ->
            assertEquals(45, subContext int AGE)
            assertEquals("Jennifer", subContext string NAME)
            assertThrows<IllegalArgumentException> { subContext double WEALTH }
        }
    }

    @Test fun `adopt changes from sub-context`() {
        Context().also { subContext ->
            subContext[AGE] = 23
            subContext[SPOUSE] = "Harold"
            c.extract(AGE, SPOUSE) from subContext
            assertEquals(23, c int AGE)
            assertEquals("Jennifer", c string NAME)
            assertEquals("Harold", c string SPOUSE)
            assertEquals(450_000.0, c double WEALTH)
        }
    }
}