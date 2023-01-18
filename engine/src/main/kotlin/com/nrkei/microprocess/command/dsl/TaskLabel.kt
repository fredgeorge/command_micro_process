/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.dsl

import com.nrkei.microprocess.command.commands.Task

// Understands which specific Task is desired
interface TaskLabel {
    val name: String
    fun task(): Task
}