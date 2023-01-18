### kotlin_template

Copyright (c) 2022 by Fred George  
author: Fred George  fredgeorge@acm.org  
Licensed under the MIT License; see LICENSE file in root.


## Starting template for Kotlin project using Gradle

Kotlin is relatively easy to setup with IntelliJ IDEA. 
Gradle is used for building and testing the project, and is a 
prerequisite. Install if necessary.
The following instructions are for installing the code 
in IntelliJ 2022 by JetBrains. 
Adapt as necessary for your environment.

Note: This implementation was setup to use:

- IntelliJ 2022.2.3 (Ultimate Edition)
- Kotlin 1.7.20
- Java SDK 19 (Oracle)
- Gradle 7.5.1
- JUnit 5.9.1 for testing

Open the reference code:

- Download the source code from github.com/fredgeorge
    - Clone, or pull and extract the zip
- Open IntelliJ
- Choose "Open" (it's a Gradle project)
- Navigate to the reference code root, and enter

Source and test directories should already be tagged as such,
with test directories in green.

Confirm that everything builds correctly (and necessary libraries exist).
There is a sample class, Rectangle, with a corresponding
test, RectangleTest. The test should run successfully
from the Gradle __test__ task.
