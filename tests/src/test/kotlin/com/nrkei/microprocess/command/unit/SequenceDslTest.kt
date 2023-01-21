/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.unit

import com.nrkei.microprocess.command.commands.ExecutionResult.*
import com.nrkei.microprocess.command.dsl.SequenceBuilder.DefaultTask.impossible
import com.nrkei.microprocess.command.dsl.SequenceBuilder.DefaultTask.unnecessary
import com.nrkei.microprocess.command.dsl.sequence
import com.nrkei.microprocess.command.util.TestAnalysis
import com.nrkei.microprocess.command.util.TestLabel.*
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
            assertEquals(3, TestAnalysis(command)[NOT_EXECUTED].size)
            assertEquals(0, TestAnalysis(command)[SUCCEEDED].size)
            assertEquals(SUCCEEDED, command.execute())
            assertEquals(0, TestAnalysis(command)[NOT_EXECUTED].size)
            assertEquals(3, TestAnalysis(command)[SUCCEEDED].size)
        }
    }

    @Test fun `Third task fails`() {
        sequence {
            first perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
            next perform SUCCESSFUL_TASK reversal unnecessary
            next perform FAILED_TASK otherwise SUCCESSFUL_RECOVERY
            next perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
        }.also { command ->
            assertEquals(4, TestAnalysis(command)[NOT_EXECUTED].size)
            assertEquals(0, TestAnalysis(command)[SUCCEEDED].size)
            assertEquals(0, TestAnalysis(command)[FAILED].size)
            assertEquals(REVERSED, command.execute())
            assertEquals(1, TestAnalysis(command)[NOT_EXECUTED].size)
            assertEquals(2, TestAnalysis(command)[REVERSED].size)
            assertEquals(1, TestAnalysis(command)[FAILED].size)
        }
    }

    @Test fun `Fourth task fails - third task reversal fails`() {
        sequence {
            first perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
            next perform SUCCESSFUL_TASK reversal unnecessary
            next perform SUCCESSFUL_TASK otherwise FAILED_RECOVERY
            next perform FAILED_TASK otherwise SUCCESSFUL_RECOVERY
        }.also { command ->
            assertEquals(REVERSAL_FAILED, command.execute())
            assertEquals(0, TestAnalysis(command)[NOT_EXECUTED].size)
            assertEquals(2, TestAnalysis(command)[REVERSED].size)
            assertEquals(1, TestAnalysis(command)[REVERSAL_FAILED].size)
            assertEquals(1, TestAnalysis(command)[FAILED].size)
        }
    }

    @Test fun `Reversal impossible`() {
        sequence {
            first perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
            next perform SUCCESSFUL_TASK reversal impossible
            next perform FAILED_TASK otherwise SUCCESSFUL_RECOVERY
        }.also { command ->
            assertEquals(REVERSAL_FAILED, command.execute())
            assertEquals(0, TestAnalysis(command)[NOT_EXECUTED].size)
            assertEquals(1, TestAnalysis(command)[REVERSED].size)
            assertEquals(1, TestAnalysis(command)[REVERSAL_FAILED].size)
            assertEquals(1, TestAnalysis(command)[FAILED].size)
        }
    }

    @Test fun `Compound sequence`() {
        sequence {
            first perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
            next perform SUCCESSFUL_TASK reversal unnecessary
            next perform sequence {
                first perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
                next perform SUCCESSFUL_TASK reversal unnecessary
                next perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
            }
            next perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
        }.also { command ->
            assertEquals(6, TestAnalysis(command)[NOT_EXECUTED].size)
            assertEquals(0, TestAnalysis(command)[SUCCEEDED].size)
            assertEquals(SUCCEEDED, command.execute())
            assertEquals(0, TestAnalysis(command)[NOT_EXECUTED].size)
            assertEquals(6, TestAnalysis(command)[SUCCEEDED].size)
        }
    }

    @Test fun `Failure in subcommand sequence`() {
        sequence {
            first perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
            next perform SUCCESSFUL_TASK reversal unnecessary
            next perform sequence {
                first perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
                next perform FAILED_TASK otherwise SUCCESSFUL_RECOVERY
                next perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
            }
            next perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
        }.also { command ->
            assertEquals(REVERSED, command.execute())
            assertEquals(2, TestAnalysis(command)[NOT_EXECUTED].size)
            assertEquals(3, TestAnalysis(command)[REVERSED].size)
            assertEquals(1, TestAnalysis(command)[FAILED].size)
        }
    }

    @Test fun `Failure after subcommand sequence`() {
        sequence {
            first perform SUCCESSFUL_TASK reversal unnecessary
            next perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
            next perform sequence {
                first perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
                next perform SUCCESSFUL_TASK reversal impossible
            }
            next perform FAILED_TASK otherwise SUCCESSFUL_RECOVERY
            next perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
        }.also { command ->
            assertEquals(REVERSAL_FAILED, command.execute())
            assertEquals(1, TestAnalysis(command)[NOT_EXECUTED].size)
            assertEquals(3, TestAnalysis(command)[REVERSED].size)
            assertEquals(1, TestAnalysis(command)[REVERSAL_FAILED].size)
            assertEquals(1, TestAnalysis(command)[FAILED].size)
        }
    }
}