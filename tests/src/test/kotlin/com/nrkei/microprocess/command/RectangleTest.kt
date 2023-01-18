/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

// Ensures Rectangle operates correctly
internal class RectangleTest {

    @Test
    fun area() {
        assertEquals(24.0, Rectangle(4.0, 6.0).area())
    }
}