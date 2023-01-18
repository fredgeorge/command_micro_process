/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command

import com.nrkei.microprocess.command.ExecutionStatus.*
import com.nrkei.microprocess.command.ExecutionStatus.FAILED
import com.nrkei.microprocess.command.util.TestTask
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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
}
