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
        // Uncomment for artifacts to be resolved from .m2 directory
        // mavenLocal()

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
    ":core:persistence",
    ":core:ui",
    ":core:model",
    ":core:repository",
    ":feature:feed",
    ":feature:article",
    ":feature:onboarding",
)