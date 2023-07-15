# RECCO GITHUB ACTION PACKAGES PUBLISHING

This document aims to explain Recco SDK publishing process using Github Actions.

## How It Works
The following .yml [file](https://github.com/sf-recco/android-sdk/blob/main/.github/workflows/release.yml) is included inside the workflows folder,
so a pipeline is generated to deal with Recco SDK publishing process.

## Triggers

Currently this workflow is only triggered if a new release (TAG) is published via the GitHub UI.

## Steps

This pipeline currently is made from the following steps.

### Checkout

Once Github Runner is in place, workspace will checkout to Recco repository.

[Checkout action reference](https://github.com/actions/checkout)

### Java Setup

Java environment setup will be provided, so both local and remote executions are provided with the same configurations.

[Java Setup action reference](https://github.com/actions/setup-java)

### Publishing

This tailored step leverages automatic token authentication by relying on the GitHub App that GitHub installs in the repository. 

- USERNAME: ${{ github.actor }}
- TOKEN: ${{ secrets.GITHUB_TOKEN }}

According to this configuration, this step is in charge of the following requirements:

- Check the latest published tag.
- Ensure both tag and moduleVersion from gradle.properties values match.
- Perform `./gradlew publishReleasePublicationToGithubPackagesRepository` task.
