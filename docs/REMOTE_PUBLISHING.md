# RECCO REMOTE GITHUB PACKAGES PUBLISHING

This document aims to explain Recco SDK publishing process using Github Packages Registry.

## How It Works

Couple of Gradle Tasks are now generated both for project and module level, these tasks are included under `publishing`folder.

**IMPORTANT:** Gradle task list is large and slow to populate in Android projects. This feature by default is disabled for performance reasons. You can re-enable it in: Settings -> Experimental -> Do not build Gradle task list during Gradle sync. Then, sync your project again.

- Module level gradle.properties file:

  All modules, are now provided with a gradle.properties file, so publishing is flexible regarding its alias.

  ```groovy
  # Convention configuration
  moduleArtifactId=recco-ui
  ```

- Project level gradle.properties file:

  Since group and version is shared among all packages, it will be set in this properties file.

  ```groovy
  ...
  moduleGroupId=com.significo
  moduleVersion=0.0.1
  ```

Make sure these files contain the information you expect your publications to attend to. The following snippet shows a possible attribute values configuration.

## Tasks

In order for Recco SDK to be published to Github Repository Registry, you should focus on the following project level task:

- **publishReleasePublicationToGithubPackagesRepository:**

  Meant to publish artifacts taking into account release buildType configuration for all modules.

  __WARNING:__ This approach is not available anymore, without specific configuration, executing this task currently will end up in failure. Remote publishing should only be triggered using Github Actions.

## Checking Results

Visit [Packages](https://github.com/orgs/viluahealthcare/packages?repo_name=android-shadowflight-sdk) section in Github.

## Usage

### Credentials

First of all, you should generate some credentials so you can work with the private repository you just uploaded you packages to.

To do so, you should read the following documentation on [personal access tokens management](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens) carefully.

You'll probably end up following these steps below:

- Visit [repository](https://github.com/viluahealthcare/android-shadowflight-sdk/)
- Access [profile](https://github.com/settings/profile)
- Open [Developer settings](https://github.com/settings/apps)
  - Tokens (classic) section
  - Generate new token (classic)

### Configuration

Now, create a file named gradle.properties under ~/.gradle/ directory.

Include the following content, so you can reference your Github user and key securely and, avoiding this data to be uploaded to any repository by mistake.

```groovy
gprUser=your-github-user-here
gprKey=your-github-key-here
```

In order to sync artifacts previously published in Github Packages repository, you need to add the following configuration.

Inside settings.gradle.kt file.

```kotlin
repositories {
  ...
  maven {
    name = "GithubPackages"
    url = uri("https://maven.pkg.github.com/viluahealthcare/android-shadowflight-sdk")
    credentials {
      username = extra["gprUser"].toString()
      password = extra["gprKey"].toString()
    }
  }
}
```

Inside settings.gradle file.

```kotlin
repositories {
  ...
  maven {
    name = "GithubPackages"
    url = uri("https://maven.pkg.github.com/viluahealthcare/android-shadowflight-sdk")
    credentials {
      username = gprUser
      password = gprKey
    }
  }
}
```

Then, include the dependencies in your module level build.gradle file and sync again.

```kotlin
  implementation("com.significo:recco-ui:0.0.1")
```
