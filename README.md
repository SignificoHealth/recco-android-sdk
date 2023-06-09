## [OpenApi](https://swagger.io/specification/)
- _Note: The API specs are auto-generated from the backend source code, so do not edit the JSON specs._
- Copy the openApi schema in the `openapi.json` file.
- Run `./gradlew openApiGenerate` to generate the API and DTOs in the `openapi` module.

You can add new custom properties in the `openapi-generator-config.json` file to customize the output content through the `.mustache` files.

## Maven Publishing

- [__Local reference__](./docs/LOCAL_PUBLISHING.md)
- [__Remote reference__](./docs/REMOTE_PUBLISHING.md)

### References:
- [Kotlin generator](https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/kotlin.md)
- [Swagger Editor](https://editor.swagger.io/)
- [OpenApi templates](https://openapi-generator.tech/docs/templating)
- [OpenApi customization](https://openapi-generator.tech/docs/customization)
- [Mustache manual](https://jgonggrijp.gitlab.io/wontache/mustache.5.html)
