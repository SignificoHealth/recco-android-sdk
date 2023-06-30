## [OpenApi](https://swagger.io/specification/)
- _Note: The API specs are auto-generated from the backend source code, so do not edit the JSON specs._
- Copy the openApi schema in the `openapi.json` file.
- Run `./gradlew openApiGenerate` to generate the API and DTOs in the `openapi` module.

You can add new custom properties in the `openapi-generator-config.json` file to customize the output content through the `.mustache` files.

#### OpenApi references:
- [Kotlin generator](https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/kotlin.md)
- [Swagger Editor](https://editor.swagger.io/)
- [OpenApi templates](https://openapi-generator.tech/docs/templating)
- [OpenApi customization](https://openapi-generator.tech/docs/customization)
- [Mustache manual](https://jgonggrijp.gitlab.io/wontache/mustache.5.html)

## Frontitude

[Frontitude](https://www.frontitude.com) provides design and product teams with a single source of truth for managing product copy.

See [**Frontitude CLI**](./docs/FRONTITUDE_CLI.md) to know how to update your `strings.xml`.

## Maven Publishing

- [__Local reference__](./docs/LOCAL_PUBLISHING.md)
- [__Remote reference__](./docs/REMOTE_PUBLISHING.md)
- [__Workflow reference__](./docs/ACTION_PUBLISHING.md)
