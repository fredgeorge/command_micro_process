/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.unit

import com.nrkei.microprocess.command.commands.ExecutionStatus.*
import com.nrkei.microprocess.command.commands.ExecutionStatus.FAILED
import com.nrkei.microprocess.command.commands.SimpleCommand
import com.nrkei.microprocess.command.util.CrashingTask
import com.nrkei.microprocess.command.util.TestTask
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

// Ensures SimpleCommands operate correctly
internal class SimpleCommandTest {

    @Test fun success() {
        assertEquals(SUCCEEDED, SimpleCommand(TestTask(SUCCEEDED)).execute())
    }

    @Test fun failure() {
        assertEquals(FAILED, SimpleCommand(TestTask(FAILED)).execute())
    }

    @Test fun suspension() {
        assertEquals(SUSPENDED, SimpleCommand(TestTask(SUSPENDED)).execute())
    }

    @Test fun crashed() {
        assertEquals(FAILED, SimpleCommand(CrashingTask).execute())
    }
}
