#!/bin/bash
# Proton Drive Linux Desktop - Build wrapper script

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

cd "$SCRIPT_DIR"

echo "Building Proton Drive for Linux..."

# Check if Java 17+ is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed. Please install Java 17 or later."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "Error: Java 17 or later is required. Current version: $JAVA_VERSION"
    exit 1
fi

# Build based on argument
case "${1:-all}" in
    deb)
        echo "Building DEB package..."
        "$PROJECT_ROOT/gradlew" :app:packageDeb
        echo "DEB package created in: app/build/compose/binaries/main/deb/"
        ;;
    rpm)
        echo "Building RPM package..."
        "$PROJECT_ROOT/gradlew" :app:packageRpm
        echo "RPM package created in: app/build/compose/binaries/main/rpm/"
        ;;
    dist)
        echo "Creating distributable..."
        "$PROJECT_ROOT/gradlew" :app:createDistributable
        echo "Distribution created in: app/build/compose/binaries/main/app/"
        ;;
    flatpak)
        echo "Building Flatpak..."
        if ! command -v flatpak-builder &> /dev/null; then
            echo "Error: flatpak-builder is not installed."
            exit 1
        fi
        # First create the distributable
        "$PROJECT_ROOT/gradlew" :app:createDistributable
        # Then build flatpak
        flatpak-builder --force-clean build-dir me.proton.Drive.yml
        flatpak build-export export build-dir
        flatpak build-bundle export proton-drive.flatpak me.proton.Drive
        echo "Flatpak created: proton-drive.flatpak"
        ;;
    run)
        echo "Running in development mode..."
        "$PROJECT_ROOT/gradlew" :app:run
        ;;
    clean)
        echo "Cleaning build artifacts..."
        "$PROJECT_ROOT/gradlew" clean
        rm -rf build-dir export *.flatpak
        ;;
    all)
        echo "Building all packages..."
        "$PROJECT_ROOT/gradlew" :app:packageDeb
        "$PROJECT_ROOT/gradlew" :app:packageRpm
        "$PROJECT_ROOT/gradlew" :app:createDistributable
        echo ""
        echo "Build complete!"
        echo "  DEB: app/build/compose/binaries/main/deb/"
        echo "  RPM: app/build/compose/binaries/main/rpm/"
        echo "  Dist: app/build/compose/binaries/main/app/"
        ;;
    *)
        echo "Usage: $0 {deb|rpm|dist|flatpak|run|clean|all}"
        echo ""
        echo "Commands:"
        echo "  deb      - Build DEB package"
        echo "  rpm      - Build RPM package"
        echo "  dist     - Create distributable directory"
        echo "  flatpak  - Build Flatpak package"
        echo "  run      - Run in development mode"
        echo "  clean    - Clean build artifacts"
        echo "  all      - Build all packages (default)"
        exit 1
        ;;
esac
