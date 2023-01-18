/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command

import com.nrkei.microprocess.command.ExecutionStatus.*
import com.nrkei.microprocess.command.ExecutionStatus.FAILED
import com.nrkei.microprocess.command.util.TestCommand
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SimpleCommandTest {

    @Test fun success() {
        assertEquals(SUCCEEDED, TestCommand(SUCCEEDED).execute())
    }

    @Test fun failure() {
        assertEquals(FAILED, TestCommand(FAILED).execute())
    }

    @Test fun suspension() {
        assertEquals(SUSPENDED, TestCommand(SUSPENDED).execute())
    }
}
