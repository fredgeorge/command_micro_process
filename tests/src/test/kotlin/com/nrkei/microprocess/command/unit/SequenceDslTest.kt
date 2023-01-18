/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.unit

import com.nrkei.microprocess.command.commands.ExecutionStatus.SUCCEEDED
import com.nrkei.microprocess.command.dsl.sequence
import com.nrkei.microprocess.command.util.TestLabel.SUCCESSFUL_RECOVERY
import com.nrkei.microprocess.command.util.TestLabel.SUCCESSFUL_TASK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

// Ensures that DSL generates SequenceCommands correctly
internal class SequenceDslTest {

    @Test fun `All tasks succeed`() {
        sequence {
            first perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
            next perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
            next perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
        }.also { command ->
            assertEquals(SUCCEEDED, command.execute())
        }
    }
}