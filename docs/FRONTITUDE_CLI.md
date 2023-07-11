## Overview
[Frontitude](https://www.frontitude.com) provides design and product teams with a single source of truth for managing product copy.

## Before starting:
- For external projects, meaning that someone invited you to this project in their workspace, in order to connect it with the CLI, you'll have to be a workspace **editor** on that workspace.
- Enable `Developer CLI` from the [Integrations tab](https://app.frontitude.com/settings/integrations)

## Fetching the latest copy:
- [Install Frontitude CLI](https://www.npmjs.com/package/@frontitude/cli): `$ npm install -g @frontitude/cli`
- Log in using your Figma account: `$ frontitude login`. To log out run: `$ frontitude logout`
- Initialize the CLI, and choose the project and the output file type: `$ frontitude init`
    - Supported file types: JSON (.json), XLIFF (.xliff, .xlf), Android XML (.xml).
- To pull the latest copy from your connected sources: `$ frontitude pull --include-translations --has-key`
    - Use `--dry-run` to display the results in the CLI without creating or updating the files

### Other commands:
- To view the list of connected sources: `$ frontitude source list`
- To set the sources that you would like to connect to your codebase: `$ frontitude source set`

## References:
- https://www.frontitude.com/blog/introducing-frontitudes-first-developer-integration
- https://www.frontitude.com/guides/developers-guide-to-connecting-the-codebase-with-frontitude
