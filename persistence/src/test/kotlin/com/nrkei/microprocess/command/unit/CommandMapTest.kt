/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.unit

import com.nrkei.microprocess.command.CommandMap
import com.nrkei.microprocess.command.commands.Context
import com.nrkei.microprocess.command.dsl.SequenceBuilder.DefaultTask.unnecessary
import com.nrkei.microprocess.command.dsl.sequence
import com.nrkei.microprocess.command.util.TestTaskLabel
import com.nrkei.microprocess.command.util.TestTaskLabel.SUCCESSFUL_RECOVERY
import com.nrkei.microprocess.command.util.TestTaskLabel.SUCCESSFUL_TASK
import org.junit.jupiter.api.Test

internal class CommandMapTest {

    @Test fun `one-level sequence`() {
        sequence {
            first perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
            next perform SUCCESSFUL_TASK reversal unnecessary
            next perform TestTaskLabel.FAILED_TASK otherwise SUCCESSFUL_RECOVERY
            next perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
        }.also { command ->
            command.execute(Context())
            println(CommandMap(command).result())
        }
    }

    @Test fun `two-level sequence`() {
        sequence {
            first perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
            next perform SUCCESSFUL_TASK reversal unnecessary
            next perform sequence {
                first perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
                next perform TestTaskLabel.FAILED_TASK otherwise SUCCESSFUL_RECOVERY
                next perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
            }
            next perform SUCCESSFUL_TASK otherwise SUCCESSFUL_RECOVERY
        }.also { command ->
            command.execute(Context())
            println(CommandMap(command).result())
        }
    }
}
