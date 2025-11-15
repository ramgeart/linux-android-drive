# Linux Port Architecture Plan

## Overview
Port Proton Drive Android app (Kotlin/Compose) to Linux x64 (Ubuntu 24.04+) with full sync features.

## Technology Stack
- **UI Framework**: Compose Multiplatform for Desktop
- **Language**: Kotlin/JVM
- **Build System**: Gradle with JVM target
- **Packaging**: Flatpak + ZIP distribution
- **CI/CD**: GitHub Actions

## Architecture Layers

### 1. Platform Layer (Linux-specific)
- File system monitoring (inotify/WatchService)
- Desktop notifications (libnotify)
- System tray integration
- Auto-start configuration
- Background service daemon

### 2. Business Logic Layer (Shared)
- Sync engine (reuse from Android)
- API client (Proton Drive SDK integration)
- Crypto operations (reuse gopenpgp)
- Database (SQLite via Room or Exposed)
- Event management

### 3. Presentation Layer
- Compose Desktop UI
- Navigation
- State management (ViewModel pattern)
- Settings UI
- Sync status UI

## Module Structure
```
linux-desktop/
├── app/                    # Main desktop application
├── platform/               # Linux platform integrations
│   ├── filesystem/        # File monitoring, operations
│   ├── notifications/     # Desktop notifications
│   ├── tray/             # System tray
│   └── daemon/           # Background service
├── sync/                  # Sync engine (adapted from Android)
├── ui/                    # Compose Desktop UI components
└── shared/               # Shared business logic
    ├── api/              # Network layer
    ├── crypto/           # Encryption/decryption
    └── database/         # Local storage
```

## Implementation Phases

### Phase 1: Project Setup
- Create Compose Multiplatform desktop module
- Setup Gradle configuration for JVM target
- Integrate Proton Drive SDK
- Setup basic app structure

### Phase 2: Core Functionality
- Port sync engine to JVM
- Implement file system watcher
- Database layer (SQLite)
- API client integration

### Phase 3: UI Development
- Main window with file browser
- System tray with quick actions
- Settings panel
- Sync status indicators
- Conflict resolution UI

### Phase 4: Linux Integration
- Desktop notifications
- Auto-start configuration
- Background daemon
- File manager integration

### Phase 5: Build & Packaging
- Flatpak manifest
- GitHub Actions workflow
- ZIP distribution with launcher scripts
- AppImage support (optional)

### Phase 6: Testing
- Unit tests for sync logic
- Integration tests
- UI tests with Compose Desktop test framework
- Manual testing on Ubuntu 24.04

## Key Dependencies
- Compose Multiplatform Desktop
- Proton Drive SDK (https://github.com/ProtonDriveApps/sdk)
- Kotlinx Coroutines
- Ktor Client (networking)
- SQLite/Exposed (database)
- Kotlinx Serialization

## Challenges & Solutions
1. **Android-specific APIs**: Replace with JVM equivalents
2. **File system monitoring**: Use Java WatchService or inotify wrapper
3. **Background service**: Systemd service or desktop autostart
4. **Notifications**: Use JNI wrapper for libnotify or pure Java solution
5. **Crypto**: Reuse gopenpgp bindings for JVM

## Success Criteria
- File sync bidirectional working
- Conflict detection and resolution
- Background sync daemon
- System tray integration
- Flatpak package builds successfully
- Works on Ubuntu 24.04+
