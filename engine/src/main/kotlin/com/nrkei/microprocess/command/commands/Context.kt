/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.command.commands

// Understands information to be shared among Tasks
class Context internal constructor(private val parameters: MutableMap<ParameterLabel, Any> = mutableMapOf()) {

    constructor(): this(mutableMapOf())

    infix fun int(label: ParameterLabel) = parameters.get(label)
        ?.let { it as Int }
        ?: throw IllegalArgumentException ("Integer parameter ${label.name} does not exist (yet)")

    infix fun string(label: ParameterLabel) = parameters.get(label)
        ?.let { it as String }
        ?: throw IllegalArgumentException ("String parameter ${label.name} does not exist (yet)")

    infix fun double(label: ParameterLabel) = parameters.get(label)
        ?.let { it as Double }
        ?: throw IllegalArgumentException ("Double parameter ${label.name} does not exist (yet)")

    operator fun set(label: ParameterLabel, value: Int) = parameters.put(label, value)

    operator fun set(label: ParameterLabel, value: String) = parameters.put(label, value)

    operator fun set(label: ParameterLabel, value: Double) = parameters.put(label, value)

    fun subset(vararg labels: ParameterLabel) = subset(labels.toList())

    private fun subset(labels: List<ParameterLabel>) =
        Context(parameters.filter{it.key in labels}.toMutableMap())

    fun extract(vararg labels: ParameterLabel) = extract(labels.toList())

    private fun extract(labels: List<ParameterLabel>) = ExtractionParameters(labels)

    inner class ExtractionParameters(private val extractionLabels: List<ParameterLabel>) {
        infix fun from(subContext: Context) {
            extractionLabels.forEach { label ->
                this@Context.parameters.put(
                    label,
                    subContext.parameters[label]
                        ?: throw IllegalArgumentException("Parameter ${label.name} does not exist in sub-Context")) }
        }
    }
}

interface ParameterLabel {
    val name: String
}