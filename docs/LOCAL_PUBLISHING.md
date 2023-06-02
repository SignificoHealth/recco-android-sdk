# SHADOWFLIGHT LOCAL MAVEN PUBLISHING

This document aims to explain Shadowflight SDK publishing process using MavenLocal.

## How It Works

Couple of Gradle Tasks are now generated both for project and module level, these tasks are included under `publishing`folder.

**IMPORTANT:** Gradle task list is large and slow to populate in Android projects. This feature by default is disabled for performance reasons. You can re-enable it in: Settings -> Experimental -> Do not build Gradle task list during Gradle sync. Then, sync your project again.

All modules, are now provided with a gradle.properties file, so local publishing is flexible regarding the group, alias and version that will be set to the artifact. 

Make sure these files contain the information you expect your publications to attend to. The following snippet shows a possible attribute values configuration.

```groovy
# Convention configuration
moduleGroupId=com.significo
moduleArtifactId=shadowflight-ui
moduleVersion=0.0.1
```

## Tasks

In order for Shadowflight SDK to be published locally, you should focus on the following project level tasks:

- **publishDebugPublicationToMavenLocal:** (INTERNAL ONLY)

  Meant to publish artifacts taking into account debug buildType configuration for all modules.

- **publishReleasePublicationToMavenLocal:**

  Meant to publish artifacts taking into account release buildType configuration for all modules.

- **publishToMavenLocal:**

  Meant to publish artifacts taking into account all variants available (defaults to release if no flavors are specified).


## Checking Results

Once any of the tasks described above is executed, you may check results inside .m2 directory under your user home directory.

You should expect to have a folder for each published module, containing the following files:

- *.sources.jar
- *.module
- *.pom
- *.aar

## Usage

In order to sync artifacts previously published in your local repository, you need to add mavenLocal() configuration as the first repository reference in your repository definitions inside settings.gradle file.

```kotlin
repositories {
    mavenLocal()
    ...
}
```

Then, include the dependencies in your module level build.gradle file.

```kotlin
    implementation("com.significo:shadowflight-ui:0.0.1")
    implementation("com.significo:shadowflight-headless:0.0.1")
```
