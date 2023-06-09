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
            url = uri("https://maven.pkg.github.com/viluahealthcare/android-shadowflight-sdk")
            credentials {
                if (extra.properties.keys.contains("gpr.user")
                    && extra.properties.keys.contains("gpr.key")
                ) {
                    username = extra["gpr.user"].toString()
                    password = extra["gpr.key"].toString()
                }
            }
        }
    }
}

rootProject.name = "android-shadowflight-sdk"

include(
    ":ui",
    //":headless",
    ":showcase",
    ":core:logger",
    ":core:openapi",
    ":core:network",
    ":core:persistence",
    ":core:ui",
    ":core:model",
    ":core:repository",
    ":feature:questionnaire",
    ":feature:feed",
    ":feature:article",
    ":feature:onboarding",
)