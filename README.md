# Android Employee Management System

[![Android CI](https://github.com/azaanmalik/android-employee-management-system/actions/workflows/android-ci.yml/badge.svg)](https://github.com/azaanmalik/android-employee-management-system/actions/workflows/android-ci.yml)

An **offline-first Android employee management application** built using **Kotlin, Room Database, Android Jetpack (ViewModel + LiveData), Material Design**, and an MVVM-inspired architecture.

The application stores employee records locally on the device. It does not require a cloud API or a separate database server.

## Features

- Create, view, update and delete employee profiles
- Employee details: name, email, phone, address, designation and salary
- Capture an employee photo with the camera or select one from the gallery
- Search by first name, last name or designation
- Sort records by name, designation or salary
- Swipe to delete and undo through a Snackbar
- Detect duplicate email addresses or phone numbers before saving
- Room-based persistence with reactive LiveData updates
- Material Design interface, splash screen and field validation

## Technology

| Area | Implementation |
| --- | --- |
| Language | Kotlin |
| UI | Android XML layouts, Material Design, ViewBinding |
| Architecture | ViewModel, Repository, DAO, observable LiveData |
| Local storage | Room over on-device SQLite |
| Images | Android camera/gallery, Coil and FileProvider |
| Build | Gradle Kotlin DSL, Android Gradle Plugin 8.3.0, Gradle wrapper 8.4 |
| Android | minSdk 24, targetSdk 34, compileSdk 34, Java 17 source compatibility |

## Project structure

```text
android-employee-management-system/
├── app/
│   ├── build.gradle.kts
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/example/employeemanager/
│   │   │   ├── data/             # Employee entity, DAO and Room database
│   │   │   ├── repository/       # Data access abstraction
│   │   │   ├── viewmodel/        # UI state, search/sorting, CRUD
│   │   │   ├── ui/               # Activities and employee list adapter
│   │   │   ├── util/             # Validation logic
│   │   │   └── SplashActivity.kt
│   │   └── res/                 # Layouts, icons, themes, menus, animations
│   ├── src/test/                # Starter unit test
│   └── src/androidTest/         # Starter Android instrumented test
├── gradle/wrapper/             # Gradle wrapper jar + properties
├── gradle/libs.versions.toml
├── gradlew / gradlew.bat
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── .github/workflows/android-ci.yml
├── LICENSE
└── README.md
```

## Getting started

### Requirements

- Android Studio with Android SDK Platform 34 and suitable build tools
- JDK 17 (select **Gradle JDK = 17** in Android Studio)
- Android emulator or physical device running **Android 7.0 (API 24)** or newer
- Internet access for the initial Gradle dependency sync

### Run locally

1. Clone the repository:
   ```bash
   git clone https://github.com/azaanmalik/android-employee-management-system.git
   ```
2. Open the **cloned repository root** in Android Studio (the directory containing `settings.gradle.kts`).
3. Allow Android Studio to download dependencies and sync Gradle.
4. Ensure Android SDK Platform 34 is installed. Let Android Studio generate local `local.properties` for your SDK path; **do not commit it**.
5. Start an Android emulator or connect a phone with USB debugging enabled.
6. Select the `app` run configuration and click **Run**.

From a terminal with the Android SDK and JDK configured:

```bash
# macOS/Linux
chmod +x ./gradlew
./gradlew :app:assembleDebug

# Windows
.\gradlew.bat :app:assembleDebug
```

The debug APK is normally produced at `app/build/outputs/apk/debug/app-debug.apk`.

### Tests

```bash
./gradlew :app:testDebugUnitTest
```

`app/src/androidTest` contains an instrumented test requiring a running emulator or connected Android device. A successful build or starter test does not prove every feature has been manually exercised.

## How it works

1. `MainActivity` shows the employee list and allows search, sorting and swipe-to-delete.
2. `AddEditEmployeeActivity` handles employee form validation, images, creation and editing.
3. `EmployeeViewModel` coordinates operations and publishes observable results.
4. `EmployeeRepository` delegates persistence to `EmployeeDao`.
5. The Room `EmployeeDatabase` stores records on the device.

## Privacy and known limitations

- Employee records include personal information and salaries. **Never commit real employee data, the local database, or production backups.** The repository only includes source and app resources.
- Local records are not synchronized across devices. The app has no remote backend, team accounts, or access-control service.
- **Important:** The supplied Room database uses `fallbackToDestructiveMigration()`. Upgrading the schema without a migration may wipe stored data. Replace it with explicit Room migrations before production use.
- The original source has been preserved. GitHub CI is intended to validate builds and starter tests, not guarantee production security or complete behavioral coverage.

## License

The repository's original software is released under the [MIT License](LICENSE), **only to the extent its contributors have authority to grant that license**. Third-party dependencies, Android assets owned by others and any separately owned materials retain their respective terms.
