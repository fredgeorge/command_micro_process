/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.unit

import com.nrkei.microprocess.command.commands.ExecutionResult.SUCCEEDED
import com.nrkei.microprocess.command.commands.SimpleCommand.CommandState
import com.nrkei.microprocess.command.dsl.SequenceBuilder.UnnecessaryPlaceHolder.unnecessary
import com.nrkei.microprocess.command.dsl.sequence
import com.nrkei.microprocess.command.util.TestAnalysis
import com.nrkei.microprocess.command.util.TestLabel.SUCCESSFUL_RECOVERY
import com.nrkei.microprocess.command.util.TestLabel.SUCCESSFUL_TASK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

// Ensures that DSL generates SequenceCommands correctly
internal class SequenceDslTest {

    @Test fun `All tasks succeed`() {
        sequence {
            first perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
            next perform SUCCESSFUL_TASK reversal unnecessary
            next perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
        }.also { command ->
            assertEquals(3, TestAnalysis(command)[CommandState.NOT_EXECUTED].size)
            assertEquals(0, TestAnalysis(command)[CommandState.SUCCESSFUL].size)
            assertEquals(SUCCEEDED, command.execute())
            assertEquals(0, TestAnalysis(command)[CommandState.NOT_EXECUTED].size)
            assertEquals(3, TestAnalysis(command)[CommandState.SUCCESSFUL].size)
        }
    }
}