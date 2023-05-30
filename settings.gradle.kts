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
    ":core:logger",
    ":core:openapi",
    ":core:network",
    ":core:ui",
    ":core:model",
    ":core:repository",
    ":feature:feed",
    ":feature:article",
    ":feature:onboarding",
)