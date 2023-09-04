# RECCO SDK: PUBLISHING A NEW RELEASE

## Create a pull request
Open a PR with the following changes:
- Update the Recco version in the top level `gradle.properties` file to the new version, following Semantic Versioning (MAJOR.MINOR.PATCH).
- In the readme file, update any references mentioning the current version and modify the URL that points to the latest Showcase app released on Bitrise.
- Add a new entry to the changelog file describing all the changes contained in this new release.

## Tag Creation and Github Action
Create a new Github release using the previously specified version, adding in the description a reference to the changelog section where the documented changes are listed.
The creation of the tag will trigger the Github action `release`, which will publish the new packages on Github Packages using the Maven publishing plugin. For additional information, refer to the [REMOTE_PUBLISHING][REMOTE-PUBLISHING] or [ACTION_PUBLISHING][ACTION-PUBLISHING] documents.

[REMOTE-PUBLISHING]:./REMOTE_PUBLISHING.md
[ACTION-PUBLISHING]:./ACTION_PUBLISHING.md
