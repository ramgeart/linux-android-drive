# Quick Start Guide - Proton Drive Linux

Get started with Proton Drive for Linux in 5 minutes!

## Prerequisites

- Java 17 or later
- Ubuntu 24.04+ (or compatible Linux distribution)
- Proton Drive account

## Installation

### Option 1: From DEB Package (Recommended for Ubuntu/Debian)

```bash
# Download the latest DEB package from releases
wget https://github.com/ramgeart/linux-android-drive/releases/latest/download/proton-drive_1.0.0_amd64.deb

# Install
sudo dpkg -i proton-drive_1.0.0_amd64.deb

# Install dependencies if needed
sudo apt-get install -f

# Run
proton-drive
```

### Option 2: From Source

```bash
# Clone the repository
git clone https://github.com/ramgeart/linux-android-drive.git
cd linux-android-drive/linux-desktop

# Run in development mode
./build.sh run
```

### Option 3: From Flatpak

```bash
# Install Flatpak (if not already installed)
sudo apt-get install flatpak

# Download the Flatpak package
wget https://github.com/ramgeart/linux-android-drive/releases/latest/download/proton-drive.flatpak

# Install
flatpak install proton-drive.flatpak

# Run
flatpak run me.proton.Drive
```

## First Run

1. **Launch the application**
   ```bash
   proton-drive
   ```

2. **Login to your Proton account**
   - Enter your Proton email and password
   - Complete 2FA if enabled

3. **Configure sync folder**
   - Default: `~/ProtonDrive`
   - Or choose a custom location

4. **Start syncing!**
   - Click "Start Sync" to begin
   - Files will sync automatically

## System Tray

The app minimizes to the system tray for easy access:

- **Left-click**: Open main window
- **Right-click**: Quick menu
  - Start/Pause Sync
  - Settings
  - Quit

## Basic Usage

### Syncing Files

1. Add files to your sync folder: `~/ProtonDrive`
2. They automatically upload to Proton Drive
3. Changes sync in both directions

### Viewing Sync Status

- Open the app
- Go to the "Sync" tab
- See real-time sync progress

### Settings

Configure:
- Sync folder location
- Auto-start on login
- Sync interval
- Notifications
- System tray behavior

## File Organization

```
~/
├── ProtonDrive/              # Your sync folder
│   ├── Documents/
│   ├── Photos/
│   └── ...
│
└── .config/
    └── proton-drive/         # App configuration
        ├── config.properties
        └── proton-drive.db
```

## Common Tasks

### Change Sync Folder

1. Open Settings
2. Click "Change Folder"
3. Select new location
4. Sync will restart

### Pause Sync

- System tray → "Pause Sync"
- Or in the app → Sync tab → "Pause"

### Upload Files

**Method 1**: Copy to sync folder
```bash
cp myfile.pdf ~/ProtonDrive/
```

**Method 2**: Use the app
- Open the app
- Go to "Upload" tab
- Click "Choose Files"

### View Synced Files

- Open the app
- Go to "Files" tab
- Browse your Proton Drive

## Troubleshooting

### App won't start

Check Java version:
```bash
java -version
# Should be 17 or later
```

Install Java 17:
```bash
sudo apt-get install openjdk-17-jdk
```

### Sync not working

1. Check internet connection
2. Verify Proton Drive credentials
3. Check sync folder permissions:
   ```bash
   ls -la ~/ProtonDrive
   ```

### View logs

```bash
# Run with debug output
./build.sh run

# Or check system logs
journalctl -f | grep proton-drive
```

### Reset configuration

```bash
# Backup first!
cp -r ~/.config/proton-drive ~/.config/proton-drive.backup

# Remove config
rm -rf ~/.config/proton-drive

# Restart app - will create fresh config
```

## Keyboard Shortcuts

- `Ctrl+Q`: Quit
- `Ctrl+S`: Settings
- `Ctrl+R`: Refresh/Sync now
- `Ctrl+P`: Pause/Resume sync

## Tips & Tricks

### Auto-start on Login

Enable in Settings → "Start on login"

Or manually create autostart entry:
```bash
mkdir -p ~/.config/autostart
cat > ~/.config/autostart/proton-drive.desktop << EOF
[Desktop Entry]
Type=Application
Name=Proton Drive
Exec=proton-drive
Hidden=false
NoDisplay=false
X-GNOME-Autostart-enabled=true
EOF
```

### Exclude Files/Folders

Create `.protondriveignore` in your sync folder:
```
# Ignore patterns (similar to .gitignore)
*.tmp
.DS_Store
node_modules/
```

### Sync Conflicts

When a file has conflicting changes:
1. App creates `filename (conflict YYYY-MM-DD).ext`
2. Review both versions
3. Keep the one you want
4. Delete the other

## Getting Help

- **Documentation**: See [README.md](README.md)
- **Development Guide**: See [DEVELOPMENT.md](DEVELOPMENT.md)
- **Issues**: https://github.com/ramgeart/linux-android-drive/issues
- **Proton Support**: https://proton.me/support/drive

## Next Steps

- Explore the **Files** view to browse your drive
- Check **Sync** status for real-time progress
- Customize **Settings** to your preference
- Enable **Notifications** to stay informed

Enjoy secure, encrypted file syncing with Proton Drive! 🚀
