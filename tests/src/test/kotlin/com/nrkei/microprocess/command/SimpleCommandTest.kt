/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command

import com.nrkei.microprocess.command.ExecutionStatus.SUCCEEDED
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SimpleCommandTest {

    @Test fun execution() {
        assertEquals(SUCCEEDED, TestCommand().execute())
    }

    private class TestCommand : Command {
        override fun execute() = SUCCEEDED

    }
}