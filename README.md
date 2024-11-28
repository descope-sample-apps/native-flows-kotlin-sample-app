# Android Sample App

This repo contains a very simple Android app that demonstrates 2 main concepts:
1. Working with `DescopeFlowView` and `DescopeFlow`
2. Showing an example of session management

## Setup

- Make sure to set the `descopeProjectId` variable in the `DemoApplication` class.
- Gradle Sync
- Run the app

## Caveats

- The code as it is assumes a simple flow is being run. Some of the possibilities that
the flow mechanism offers require a bit of setup, both on the Android side and on the
Descope console. You can change the `?flow=` query parameter to choose a different flow.
Read more about the abilities and required setup in the [Descope SDK repo README and
documented classes](https://github.com/descope/descope-kotlin).
- The code as it is, is meant to be viewed as a simplified usage reference. It is intentionally
a lean representation of some of the main concepts in the Descope Android SDK
and a general usage example, and not what a fully fledged app usually looks like.
This is done for brevity and code-readability.

## Additional Information

To learn more please see the [Descope Documentation and API reference page](https://docs.descope.com/).

## Contact Us

If you need help you can email [Descope Support](mailto:support@descope.com)

## License

The Descope SDK for Android and Android Sample App are licensed for use under the terms and conditions
of the [MIT license Agreement](https://github.com/descope/descope-android/blob/main/LICENSE).
