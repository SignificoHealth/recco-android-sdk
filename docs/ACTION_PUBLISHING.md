# SHADOWFLIGHT GITHUB ACTION PACKAGES PUBLISHING

This document aims to explain Shadowflight SDK publishing process using Github Actions.

## How It Works

The following .yml [file](https://github.com/viluahealthcare/android-shadowflight-sdk/blob/feature/SHAD-142/.github/workflows/android-shadowflight-sdk_prod.yml) is included inside the workflows folder,
so a pipeline is generated to deal with Shadowflight SDK publishing process.

## Triggers

Currently this workflow is only triggered if a new TAG is published to the repository.

## Steps

This pipeline currently is made from the following steps.

### Checkout

Once Github Runner is in place, workspace will checkout to Shadowflight repository.

[Checkout action reference](https://github.com/actions/checkout)

### Java Setup

Java environment setup will be provided, so both local and remote executions are provided with the same configurations.

[Java Setup action reference](https://github.com/actions/setup-java)

### Publishing

This is a tailored step in which the following secrets are provided:

- USERNAME: ${{ secrets.USERNAME }}
- TOKEN: ${{ secrets.GHPR_PAT }}

According to this configuration, this step is in charge of the following requirements:

- Check the latest published tag.
- Ensure both tag and moduleVersion from gradle.properties values match.
- Perform `./gradlew publishReleasePublicationToGithubPackagesRepository` task.
