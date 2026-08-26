# Fix Gradle Sync Error: Cannot add extension with name 'kotlin'

The error occurs because the Kotlin plugin version (`1.9.24`) is incompatible with the futuristic Gradle (`9.5.0`) and Android Gradle Plugin (`9.3.2`) versions being used. In Gradle 9.x, the extension registration system has changed, and older Kotlin plugins may attempt to register the `kotlin` extension in a way that clashes with the new environment or the Android Gradle Plugin.

## Proposed Changes

### Root Project Configuration

#### [MODIFY] [build.gradle](file:///C:/Users/Dell/Documents/GitHub/OBD2Diagnostic/build.gradle)
- Upgrade Kotlin plugin version from `1.9.24` to `2.4.10` (the stable version for this environment).
- Add `org.jetbrains.kotlin.kapt` to the top-level plugins block to manage its version centrally.

### App Module Configuration

#### [MODIFY] [app/build.gradle](file:///C:/Users/Dell/Documents/GitHub/OBD2Diagnostic/app/build.gradle)
- Align the Kotlin Kapt plugin ID with the full form `org.jetbrains.kotlin.kapt`.
- (Optional but recommended) Upgrade Room dependencies to `2.8.4` for better compatibility with Kotlin 2.x.

## Verification Plan

### Automated Tests
- Trigger a Gradle Sync in Android Studio to verify the error is resolved.
- Run `./gradlew :app:assembleDebug` to ensure the project builds correctly with the new Kotlin version.
