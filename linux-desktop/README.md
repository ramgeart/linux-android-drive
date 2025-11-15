# Proton Drive for Linux

Proton Drive desktop application for Linux x64 (Ubuntu 24.04+) with full sync features.

## Features

- **Secure Sync**: End-to-end encrypted file synchronization
- **Real-time Monitoring**: Automatic file change detection
- **System Tray Integration**: Quick access to sync status and controls
- **Desktop Notifications**: Stay informed about sync progress
- **Conflict Resolution**: Smart handling of file conflicts
- **Background Daemon**: Runs in the background for continuous sync

## Technology Stack

- **UI**: Compose Multiplatform for Desktop
- **Language**: Kotlin/JVM
- **Platform**: Linux x64
- **Encryption**: Proton's end-to-end encryption

## Requirements

- Ubuntu 24.04 or later (or compatible Linux distribution)
- Java 17 or later
- 4GB RAM minimum (8GB recommended)
- Active Proton Drive account

## Installation

### From DEB package (Ubuntu/Debian)
```bash
sudo dpkg -i proton-drive_1.0.0_amd64.deb
sudo apt-get install -f  # Install dependencies if needed
```

### From RPM package (Fedora/RHEL)
```bash
sudo rpm -i proton-drive-1.0.0.x86_64.rpm
```

### From Flatpak
```bash
flatpak install proton-drive.flatpak
```

### From ZIP distribution
```bash
unzip proton-drive-linux-x64.zip
cd proton-drive-linux
./bin/proton-drive
```

## Building from Source

### Prerequisites
- JDK 17 or later
- Gradle 8.x (included via wrapper)

### Build Steps

1. Clone the repository:
```bash
git clone https://github.com/ramgeart/linux-android-drive.git
cd linux-android-drive/linux-desktop
```

2. Build the application:
```bash
../gradlew :app:packageDeb        # Build DEB package
../gradlew :app:packageRpm        # Build RPM package
../gradlew :app:createDistributable  # Create runnable distribution
```

3. Run in development mode:
```bash
../gradlew :app:run
```

## Architecture

The application is organized into several modules:

- **app**: Main desktop application with Compose UI
- **platform**: Linux-specific integrations
  - **filesystem**: File monitoring and operations
  - **notifications**: Desktop notifications
  - **tray**: System tray integration
  - **daemon**: Background service
- **sync**: Synchronization engine
- **ui**: Shared UI components
- **shared**: Common business logic
  - **api**: Network layer for Proton Drive API
  - **crypto**: Encryption/decryption operations
  - **database**: Local storage

## Configuration

Configuration files are stored in:
```
~/.config/proton-drive/
```

Sync folder default location:
```
~/ProtonDrive/
```

## Development

### Project Structure
```
linux-desktop/
├── app/                    # Main application
├── platform/               # Platform integrations
├── sync/                   # Sync engine
├── ui/                     # UI components
└── shared/                 # Shared modules
```

### Running Tests
```bash
../gradlew test
```

### Code Style
This project follows Kotlin coding conventions and uses detekt for static analysis.

## Contributing

This is a port of the Android version. Contributions are welcome! Please follow the existing code structure and conventions.

## License

Copyright (c) 2025 Proton AG

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

See [LICENSE](../LICENSE) for more details.

## Acknowledgments

- Based on [Proton Drive Android](https://github.com/ProtonDriveApps/android-drive)
- Uses [Proton Drive SDK](https://github.com/ProtonDriveApps/sdk)
- Built with [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)

## Support

For issues and feature requests, please use the GitHub issue tracker.

For general Proton Drive support, visit: https://proton.me/support/drive
