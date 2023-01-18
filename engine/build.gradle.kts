/*
 * Copyright (c) 2022 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */
 
plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    kotlin("jvm")

    // Apply the java-library plugin for API and implementation separation.
    `java-library`

    // Collect metrics on the exercises
    id("io.gitlab.arturbosch.detekt").version("1.21.0")
}

repositories {
    mavenLocal()
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.7.20"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.20")

    // Use counting metrics Detekt plugin
    detektPlugins("com.github.fredgeorge.detektmethodmcc:detekt-method-mcc:1.1")
}

detekt {
    toolVersion = "1.21.0"
    config = files("config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}
