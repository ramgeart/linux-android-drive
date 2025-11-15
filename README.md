# Proton Drive

This repository contains:
- **Android version**: Original Proton Drive for Android (Kotlin/Android)
- **Linux Desktop version**: NEW - Proton Drive for Linux x64 (Kotlin/Compose Multiplatform)

Copyright (c) 2023-2025 Proton AG

---

## 🐧 Linux Desktop Version (NEW!)

**[Go to Linux Desktop →](linux-desktop/)**

A native desktop application for Linux x64 (Ubuntu 24.04+) with full sync features.

### Quick Start (Linux)

```bash
cd linux-desktop
./build.sh run
```

See [linux-desktop/README.md](linux-desktop/README.md) for full documentation.

---

## 📱 Android Version

## License

The code and data files in this distribution are licensed under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. See <https://www.gnu.org/licenses/> for a copy of this license.

See [LICENSE](LICENSE) file

## Setup

The most straightforward way to build and run this application is to:

- Install Android Studio: https://developer.android.com/studio/install
- Clone the repository. You have two options:
	- Use the `Project from version control` in Android Studio, or
	- Use the `git clone` command and import it into Android Studio
- Build and run the app directly in Android Studio

Alternatively, if you want to build the app directly from the command line (or using a different IDE, etc.), you will first need to install the command line tools from: https://developer.android.com/studio#cmdline-tools. Then you will need to install the SDK using the `sdkmanager` tool. After cloning the repository with `git clone` you will need to edit the `local.properties` file so that it points to the location of the SDK. Depending on which operating systems you use, the location of the SDK is usually:

- Windows: `C:\Users\<username>\AppData\Local\Android\sdk`
- MacOS: `/Users/<username>/Library/Android/Sdk/`
- Linux: `/home/<username>/Android/Sdk/`

Then, go to the app’s root directory in the command line tool and run:

- `./gradlew assembleProdDebug`
- `adb install ./app/build/outputs/apk/prod/debug/ProtonDrive-1.X.X-prod-debug.apk`

## Contributions

No contributions are accepted for now.

Copyright (c) 2023 Proton AG

