pluginManagement {
    includeBuild("build-logic")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {

    val githubUsername = if (extra.properties.keys.contains("gprUser")) {
        extra["gprUser"].toString()
    } else {
        System.getenv("USERNAME")
    }

    val githubSecret = if (extra.properties.keys.contains("gprKey")) {
        extra["gprKey"].toString()
    } else {
        System.getenv("GHPR_PAT")
    }

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
                username = githubUsername
                password = githubSecret
            }
        }
    }
}

rootProject.name = "android-recco-sdk"

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
include(":core:base")
