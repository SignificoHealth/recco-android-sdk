# ENSURE QUALITY CODE WITH STATUS CHECKS

This document gathers the status checks that are executed automatically as part of the [PULL_REQUEST][PULL-REQUESTS] workflow to ensure the security and quality of new changes.

## Lint
[Lint][Lint] helps to enhance code quality and readability, ensuring coding consistency and standards. It also helps to identify/optimizes performance issues and detects/mitigates security vulnerabilities. In short, it facilitates code maintainability and productivity.

Recommended to run `./gradlew lint` locally prior to pushing changes.

## Spotless
[Spotless][Spotless] checks ensure that code always adheres to the defined style rules. This improves code readability and makes it easier for developers to work on different parts of the codebase without encountering formatting discrepancies. 

Run `./gradlew spotlessCheck` to detect issues or `./gradlew spotlessApply` to apply fixing for the issues detected automatically. Recommended to run them locally prior to pushing changes.

## Unit tests
The project is covered by a set of unit tests to ensure code quality, improve collaboration as it allows to refactoring with confidence, prevent regressions with early bug detection, and ultimately delivering a more reliable and robust application.

Recommended to run `./gradlew test` locally prior to pushing changes.

[PULL-REQUESTS]:./docs/CONTRIBUTIONS_PULL_REQUESTS.md
[Lint]:https://developer.android.com/studio/write/lint
[Spotless]:https://github.com/diffplug/spotless
