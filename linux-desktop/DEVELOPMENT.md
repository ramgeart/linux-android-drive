# Development Guide - Proton Drive Linux

## Quick Start

### Prerequisites
- JDK 17 or later
- Gradle 8.x (included via wrapper)
- Ubuntu 24.04+ or compatible Linux distribution

### Running the Application

```bash
# Run in development mode
./build.sh run

# Or directly with gradle
./gradlew :app:run
```

### Building Packages

```bash
# Build all packages
./build.sh all

# Build specific package
./build.sh deb      # Debian package
./build.sh rpm      # RPM package
./build.sh dist     # Standalone distribution
./build.sh flatpak  # Flatpak (requires flatpak-builder)
```

## Project Structure

```
linux-desktop/
├── app/                           # Main desktop application
│   ├── src/jvmMain/
│   │   ├── kotlin/
│   │   │   └── me/proton/drive/linux/
│   │   │       ├── Main.kt       # Application entry point
│   │   │       └── ConfigManager.kt
│   │   └── resources/
│   │       └── icon.svg          # Application icon
│   └── build.gradle.kts
│
├── platform/                      # Linux platform integrations
│   ├── filesystem/                # File system monitoring
│   │   └── src/main/kotlin/.../FileSystemWatcher.kt
│   ├── notifications/             # Desktop notifications
│   │   └── src/main/kotlin/.../NotificationService.kt
│   ├── tray/                      # System tray integration
│   │   └── src/main/kotlin/.../SystemTrayManager.kt
│   └── daemon/                    # Background daemon service
│
├── sync/                          # Synchronization engine
│   └── src/main/kotlin/.../SyncEngine.kt
│
├── ui/                            # Reusable UI components
│
└── shared/                        # Shared business logic
    ├── api/                       # Proton Drive API client
    │   └── src/main/kotlin/
    │       ├── ProtonDriveApiClient.kt
    │       └── Models.kt
    ├── crypto/                    # Encryption/decryption
    │   └── src/main/kotlin/.../CryptoUtils.kt
    └── database/                  # Local database
        └── src/main/kotlin/.../DatabaseManager.kt
```

## Development Workflow

### 1. Making Changes

Edit the Kotlin source files in the respective modules. The project uses:
- **Compose Multiplatform** for UI
- **Kotlin Coroutines** for async operations
- **Ktor Client** for HTTP networking
- **Exposed** for database operations

### 2. Testing

```bash
# Run all tests
./gradlew test

# Run tests for specific module
./gradlew :sync:test
./gradlew :app:test
```

### 3. Code Style

The project follows Kotlin coding conventions. Use:

```bash
# Check code style (when detekt is configured)
./gradlew detekt
```

## Key Components

### File System Watcher

Location: `platform/filesystem/src/main/kotlin/.../FileSystemWatcher.kt`

Uses Java's `WatchService` to monitor file system changes:

```kotlin
val watcher = FileSystemWatcher(Paths.get("/path/to/watch"))
watcher.watchEvents().collect { event ->
    when (event) {
        is FileSystemEvent.Created -> // Handle file creation
        is FileSystemEvent.Modified -> // Handle file modification
        is FileSystemEvent.Deleted -> // Handle file deletion
    }
}
```

### Sync Engine

Location: `sync/src/main/kotlin/.../SyncEngine.kt`

Coordinates synchronization between local and remote:

```kotlin
val syncEngine = SyncEngine(localSyncPath)
syncEngine.startSync()

syncEngine.syncState.collect { state ->
    when (state) {
        is SyncState.Syncing -> // Show progress
        is SyncState.Error -> // Handle error
        is SyncState.Idle -> // Sync complete
    }
}
```

### Database Manager

Location: `shared/database/src/main/kotlin/.../DatabaseManager.kt`

Manages local metadata storage:

```kotlin
val db = DatabaseManager(configManager.getDatabasePath())
db.insertFile(linkId, localPath, hash, size, modTime)
val file = db.getFileByLinkId(linkId)
```

### API Client

Location: `shared/api/src/main/kotlin/.../ProtonDriveApiClient.kt`

Communicates with Proton Drive API:

```kotlin
val apiClient = ProtonDriveApiClient()
val result = apiClient.authenticate(username, password)
val files = apiClient.listFiles(shareId, parentLinkId)
```

### System Tray

Location: `platform/tray/src/main/kotlin/.../SystemTrayManager.kt`

Provides system tray integration:

```kotlin
val trayManager = SystemTrayManager()
trayManager.initialize(
    onOpen = { /* Show window */ },
    onSync = { /* Start sync */ },
    onPause = { /* Pause sync */ },
    onSettings = { /* Show settings */ },
    onQuit = { /* Exit app */ }
)
```

## Configuration

The application stores configuration in `~/.config/proton-drive/`:

- `config.properties` - User preferences
- `proton-drive.db` - Local database

### Default Settings

```properties
sync.folder=~/ProtonDrive
sync.auto_start=true
sync.interval_seconds=300
ui.minimize_to_tray=true
ui.show_notifications=true
```

## Integration Points

### Proton Drive SDK

The SDK integration is planned for the `shared/api` module. See [SDK_INTEGRATION.md](SDK_INTEGRATION.md) for details.

### GopenPGP

Crypto operations will use the existing `gopenpgp` module from the Android project. This needs to be adapted for JVM usage.

## Debugging

### Enable Logging

Add to your run configuration:

```bash
./gradlew :app:run --debug
```

### Debug Build

```bash
./gradlew :app:run --stacktrace
```

### Database Inspection

Use SQLite tools to inspect the database:

```bash
sqlite3 ~/.config/proton-drive/proton-drive.db
```

## CI/CD

The project uses GitHub Actions for automated builds. See `.github/workflows/build-linux-desktop.yml`.

The workflow builds:
- DEB packages
- RPM packages
- ZIP distributions
- Flatpak (planned)

## Next Steps

1. **Implement API Integration**: Complete the Proton Drive API client
2. **Add Crypto**: Integrate GopenPGP for encryption
3. **Complete Sync**: Implement bidirectional sync logic
4. **Enhance UI**: Add file browser and detailed sync status
5. **Add Tests**: Comprehensive unit and integration tests

## Troubleshooting

### Build Fails

```bash
# Clean build
./build.sh clean
./gradlew clean
```

### Missing Dependencies

```bash
# On Ubuntu/Debian
sudo apt-get install openjdk-17-jdk

# Verify Java version
java -version
```

### Runtime Issues

Check logs in:
- Console output when running via `./build.sh run`
- System journal: `journalctl -f`

## Contributing

1. Follow Kotlin coding conventions
2. Add tests for new features
3. Update documentation
4. Test on Ubuntu 24.04+

## Resources

- [Compose Multiplatform Docs](https://github.com/JetBrains/compose-multiplatform)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Ktor Client](https://ktor.io/docs/client.html)
- [Exposed ORM](https://github.com/JetBrains/Exposed)
