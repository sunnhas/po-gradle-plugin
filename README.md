# PoGradle

## Description
The PoGradle is a Gradle plugin designed to seamlessly integrate with PoEditor, a localization management platform.
This plugin simplifies the process of managing translations and integrating them into your Gradle-based project.
With PoGradle, you can easily automate the synchronization of translations between your project and PoEditor, streamlining the localization workflow for your applications.

## Installation
To use this plugin, add the following to your build.gradle file:

```kotlin
plugins {
    id('io.github.sunnhas.po-gradle') version '1.0.0'
}
```

## Usage

To use this plugin it's required to create an api token for PoEditor.
It should be set as:

```kotlin
poGradle {
    apiToken.set("generated-token")
}
```

Tasks inherited with this plugin is build dynamically based on the projects configured in the `poGradle` configuration block.

```kotlin
poGradle {
    projects {
        create("projectName") {
            // Your project configuration
        }
    }
}
```

This will create tasks for the configured project.

### Download translations
For each project, the plugin will create a download task to download all translations of the specified languages.
Besides that, you will also get tasks for each language specified.

The task name is called `poDownload{TaskName}`, e.g. `poDownloadProjectName` with above example.

### Example configuration

```kotlin
import io.github.sunnhas.poeditor.config.Format

poGradle {
    apiToken.set("some-token")

    projects {
        create("example") {
            projectId.set(47138441)
            languages.set(listOf("en"))
            format.set(Format.PROPERTIES)
            output.set(projectDir.resolve("src/main/resources/translations"))
        }
    }
}
```

See the [example](example) project for more examples.

## License
This plugin is build under the Apache-2 lincese. See [LICENSE](LICENSE) for full description.

## Contributing
We welcome and appreciate contributions from the community.
Before contributing, please take a moment to review the following guidelines.

### How to Contribute
1. Fork the repository and clone it to your local machine.
2. Create a new branch for your contribution: git checkout -b feature/your-feature-name.
3. Make your changes and ensure that the code follows the project's coding standards.
4. Write tests to cover your changes, if applicable.
5. Run the existing tests and ensure that they pass: ./gradlew test.
6. Commit your changes and push to your fork: git push origin feature/your-feature-name.
7. Submit a pull request to the main repository.

### Code Style
We follow the official Kotlin code style guidelines for this project.
Please ensure that your code adheres to these guidelines.

### Reporting Issues
If you encounter any issues or have feature requests, please open an issue in the repository's issue tracker.
Please provide detailed information about the problem and steps to reproduce it.

### Code Reviews
All contributions will go through a code review process. Please be open to feedback and be willing to make changes to your code based on the review.

### License
By contributing to this project, you agree that your contributions will be licensed under the project's license.

Thank you for considering contributing to the PoGradle plugin.
Your help is greatly appreciated!
