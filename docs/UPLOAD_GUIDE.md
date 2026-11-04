# Completing the portfolio upload

The GitHub scaffold (README, MIT license, security guidance and CI) is committed directly through the connected GitHub integration. The source upload package contains **all 60 meaningful original project files** from `EmployeeManager.zip` in their expected root-relative paths. The `.gitignore` in that package is the final version used by the repository.

1. Download `EmployeeManager-Professional-GitHub-Upload.zip` from the ChatGPT conversation.
2. In GitHub Desktop, clone `https://github.com/azaanmalik/android-employee-management-system.git`.
3. Extract **the contents of the ZIP**, not the ZIP itself, into the repository root. The root must contain `app/`, `gradle/`, `gradlew`, `gradlew.bat`, `settings.gradle.kts`, `README.md`, `LICENSE`, etc. Do not add an `EmployeeManager/` wrapper folder.
4. GitHub Desktop should detect the original source, resources, Gradle wrapper files and possibly a `.gitignore` update. Review changes and commit/push.
5. Open the GitHub Actions tab for the latest commit. The workflow runs a Gradle debug build and local unit tests after source files have been pushed.

Intentionally **excluded**: `.gradle/`, `build/`, `app/build/`, `.idea/`, local SDK path `local.properties`, transient caches and compiled artifacts. None is needed for normal Android Studio import.

**Note:** The package includes the original `gradle/wrapper/gradle-wrapper.jar` (an essential wrapper dependency), and the original Kotlin/XML source and PNG/WebP resources. No source code files from the allowed set were removed.
