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
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Needs exercises to test
    implementation(project(":engine"))

    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.7.20"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.20")

    // Jupiter using JUnit 5
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
	testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
}

tasks.test {
    useJUnitPlatform()  // Encourages(?) JUnit 5 use by Kotlin
    testLogging {
        events("passed", "skipped", "failed")
    }
}
