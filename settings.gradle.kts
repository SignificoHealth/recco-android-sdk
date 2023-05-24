pluginManagement {
    includeBuild("build-logic")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "android-shadowflight-sdk"

include(
    ":ui",
    ":headless",
    ":showcase",
    ":core:network",
    ":core:ui",
    ":feature:feed",
    ":feature:article",
    ":feature:onboarding"
)
