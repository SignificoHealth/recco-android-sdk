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
        maven(url = "https://jitpack.io")

        maven {
            name = "GithubPackages"
            url = uri("https://recco-maiden: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@maven.pkg.github.com/sf-recco/android-sdk")
        }
    }
}

rootProject.name = "recco-android-sdk"

include(
    "api:ui",
    "api:model",
    "showcase",
    "internal:core:base",
    "internal:core:logger",
    "internal:core:openapi",
    "internal:core:network",
    "internal:core:persistence",
    "internal:core:ui",
    "internal:core:model",
    "internal:core:repository",
    ":internal:core:test",
    "internal:feature:questionnaire",
    "internal:feature:feed",
    "internal:feature:article",
    "internal:feature:onboarding",
    "internal:feature:bookmark"
)