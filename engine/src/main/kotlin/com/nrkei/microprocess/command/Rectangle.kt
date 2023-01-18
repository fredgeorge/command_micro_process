/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command

// Understands a four-sided polygon with sides at right angles
class Rectangle(private val length: Double, private val width: Double) {
    fun area() = length * width
}
