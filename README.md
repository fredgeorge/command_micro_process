### command_micro_process

Copyright (c) 2023 by Fred George  
author: Fred George  fredgeorge@acm.org  
Licensed under the MIT License; see LICENSE file in root.


## Command MicroProcess Framework in Kotlin

The Command MicroProcess Framework supports processes that must
proceed in a sequential fashion. The next step in the process should
not be attempted until the prior step is successful. This models many
typical business processes such as account setup.

This framework separates doing the work (via Tasks) and the flow of
the work (via Commands and sequences of Commands). This separation of
concerns facilitates customization and reduces coupling.

### Framework Setup

Kotlin is relatively easy to setup with IntelliJ IDEA. 
Gradle is used for building and testing the project, and is a 
prerequisite. Install if necessary.
The following instructions are for installing the code 
in IntelliJ 2022 by JetBrains. 
Adapt as necessary for your environment.

Note: This implementation was setup to use:

- IntelliJ 2023.3.6 (Ultimate Edition)
- Kotlin 1.9.23
- Java SDK 21 (Oracle)
- Gradle 8.7
- JUnit 5.10.2 for testing
- Jackson for Kotlin 2.17.0

Open the reference code:

- Download the source code from github.com/fredgeorge
    - Clone, or pull and extract the zip
- Open IntelliJ
- Choose "Open" (it's a Gradle project)
- Navigate to the reference code root, and press enter

Source and test directories should already be tagged as such,
with test directories in green.

Confirm that everything builds correctly (and necessary libraries exist).
Run all the unit tests.

### Framework Design

This MicroProcess framework extensively exploits Design Patterns (GoF).
The base pattern is Command Pattern (GoF, an encapsulation of code to 
be executed at some point in the future. Since it understands that 
execution, it is also responsible for reversing that execution, if 
necessary (and possible).

Commands can be chained together to build sequences. Here we also want
to support a Command that itself is another sequence of Commands. Hence
the Composite Pattern (GoF) is used. A Composite Command pattern is 
also known as a Macro.

We want to be able to suspend a Command sequence if we need to wait
for an asynchronous operation to complete. To facilitate resumption, we
restart the sequence, skipping over Commands that are already complete.
So we need to remember whether a Command has been executed already; we 
exploit a State Pattern (GoF) for this.

A Command can succeed, fail, or be suspended. If a Command in a sequence
fails, all prior Commands in the sequence will be _undone_, starting
with the last Command to successfully execute.

We want to further separate behavior from flow. Composite Commands
capture the flow. The behavior is delegated for primitive Commands to
a task. An application designer can therefore:

- Modify the flow without needing to change the behavior, or
- Modify behavior without impacting the flow.

If your domain requires customization, one or both of these will
prove useful. A catalog of behaviors can be established, and subsets
used for a client. Conversely, if one client has a different flow of 
work and approvals, that can be captured by simply altering the
Commands.

For inspection, analysis, persistence, and other functions against a 
Command hierarchy, we use a Visitor Pattern (GoF). In testing, the 
visitor can sweep through the hierarchy and collect state for validation
assertion.

Tasks in general will need to share information. For example, the result
of one task is necessary to feed another task. To support this, we use
a Collecting Parameter Pattern (C2.com) called a Context. Sharing data
through a Context is a bit of an anti-pattern; it can become a dumping 
ground (it's just a big map), create excessive coupling, and compromise
the granularity we are seeking. To mitigate this, each task must declare
the information it needs from the overall Context, and which information
it will contribute (add or change) to the overall Context). This enables
some straightforward static analysis of usage patterns (not implemented
here).
