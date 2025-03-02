![image](https://github.com/user-attachments/assets/28135c91-bfab-4195-a20d-4a10a3f33886)

# Descope's Native Flows Kotlin Sample App

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Welcome to Descope's Native Flows Kotlin Sample App, a demonstration of how to integrate Descope native flows for user authentication within a Kotlin application. By exploring this project, you can understand how Descope works with Kotlin to manage native flows. For an example with all authentication methods, refer to the [Kotlin Sample App](https://github.com/descope-sample-apps/kotlin-sample-app).

## Features
This sample app includes:

- **App Client**: An example of how the client communicates with Descope.

## Getting Started
Follow these steps to run the sample app and explore Descope's capabilities with Kotlin:

### Prerequisites
Make sure you have the following installed:

- Android Studio
- Your Project ID which you can get in the [Project Settings](https://app.descope.com/settings/project)

### Run the app

1. Clone this repo
2. Open the project within Android Studio
3. Within the `DemoApplication.kt` file of the project, change the `descopeProjectId` (If in a non-US region, or using a custom domain with CNAME, uncomment `baseUrl` and replace `myBaseURL` with your specific localized base URL)

4. **(Optional) Self-Host Your Flow**: Your Descope authentication flow is automatically hosted by Descope at `https://api.descope.com/login/<your_descope_project_id>` but you can use your own website or domain to host your flow. You can modify the value for the flow Url in the `LoginActivity.kt` file to include your own hosted page with our Descope Web Component, as well as alter the `?flow=sign-up-or-in` parameter to run a different flow.

```
val descopeFlow = DescopeFlow("https://api.descope.com/login/$descopeProjectId?flow=sign-up-or-in&shadow=false")
```

> For more information about Auth Hosting, visit our docs on it [here](https://docs.descope.com/auth-hosting-app)

5. Run the simulator within Android Studio - The play button located in the top right


## Learn More
To learn more please see the [Descope Documentation and API reference page](https://docs.descope.com/).

## Contact Us
If you need help you can [contact us](https://docs.descope.com/support/)

## License
Descope's Native Flows Kotlin Sample App is licensed for use under the terms and conditions of the MIT license Agreement.
