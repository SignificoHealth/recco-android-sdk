## Development

- Entirely written in [Kotlin](https://kotlinlang.org/).
- UI completely written in [Jetpack Compose](https://developer.android.com/jetpack/compose).
- Follows the [guide to app architecture](https://developer.android.com/jetpack/guide).
- Uses [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-guide.html) throughout.
- Uses [Hilt](https://dagger.dev/hilt) for dependency injection.
- Uses [Coil](https://coil-kt.github.io/coil/compose/) for image loading.

## [OpenApi](https://swagger.io/specification/)
- _Note: The API specs are auto-generated from the backend source code, so do not edit the JSON specs._
- Copy the openApi schema in the `openapi.json` file.
- Run `./gradlew openApiGenerate` to generate the API and DTOs in the `openapi` module.

You can add new custom properties in the `openapi-generator-config.json` file to customize the output content through the `.mustache` files.

### OpenApi references:
- [Kotlin generator](https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/kotlin.md)
- [Swagger Editor](https://editor.swagger.io/)
- [OpenApi templates](https://openapi-generator.tech/docs/templating)
- [OpenApi customization](https://openapi-generator.tech/docs/customization)
- [Mustache manual](https://jgonggrijp.gitlab.io/wontache/mustache.5.html)

## Frontitude

[Frontitude](https://www.frontitude.com) provides design and product teams with a single source of truth for managing product copy.

See [**Frontitude CLI**](./FRONTITUDE_CLI.md) to know how to update your `strings.xml`.

## Releasing a new SDK version

- [__Release__](./RELEASE.md)
- [__Workflow reference__](./ACTION_PUBLISHING.md)
- [__Remote reference__](./REMOTE_PUBLISHING.md)
- [__Local reference__](./LOCAL_PUBLISHING.md)

## Spotless integration

[Spotless](https://github.com/diffplug/spotless) plugin for Gradle.

Run `./gradlew spotlessCheck` to detect issues or `./gradlew spotlessApply` to apply fixing for the issues detected automatically.

Recommended to run locally before pushing changes.

## Keep a changelog

Standardized open source project CHANGELOG [guide](https://keepachangelog.com/en/1.1.0/)
