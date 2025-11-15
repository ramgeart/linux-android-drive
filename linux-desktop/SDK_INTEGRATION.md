# Proton Drive SDK Integration Guide

## Overview

This document outlines how to integrate the [Proton Drive SDK](https://github.com/ProtonDriveApps/sdk) into the Linux desktop application.

## SDK Integration Steps

### 1. Add SDK Dependency

The Proton Drive SDK needs to be added as a dependency to the `shared/api` module:

```kotlin
// In shared/api/build.gradle.kts
dependencies {
    // TODO: Add Proton Drive SDK when available for JVM
    // implementation("me.proton.drive:sdk-jvm:x.x.x")
    
    // For now, we'll need to adapt the Android SDK or use the API directly
    implementation("io.ktor:ktor-client-core:3.0.1")
    implementation("io.ktor:ktor-client-auth:3.0.1")
    implementation("io.ktor:ktor-client-content-negotiation:3.0.1")
}
```

### 2. API Client Implementation

Create an API client that interfaces with Proton Drive's REST API:

- **Authentication**: Handle Proton account login (SRP protocol)
- **Session Management**: Maintain authentication tokens
- **File Operations**: Upload, download, list files
- **Folder Operations**: Create, delete, move folders
- **Share Management**: Handle shared links and permissions

### 3. Crypto Integration

The SDK includes encryption/decryption functionality:

- **GopenPGP**: Use the existing `gopenpgp` module from the Android project
- **Key Management**: Securely store and retrieve encryption keys
- **File Encryption**: Encrypt files before upload
- **File Decryption**: Decrypt downloaded files

### 4. Sync Protocol

Implement the sync protocol:

1. **Initial Sync**: Download remote file tree and metadata
2. **Change Detection**: Monitor local and remote changes
3. **Conflict Resolution**: Handle file conflicts
4. **Delta Sync**: Only sync changed files

## API Endpoints (Reference)

Based on Proton Drive API:

- `POST /api/auth` - Authentication
- `GET /api/drive/volumes` - List volumes
- `GET /api/drive/volumes/{volumeId}/shares` - List shares
- `GET /api/drive/shares/{shareId}/links` - List files/folders
- `POST /api/drive/shares/{shareId}/links` - Create file/folder
- `PUT /api/drive/shares/{shareId}/links/{linkId}` - Update metadata
- `DELETE /api/drive/shares/{shareId}/links/{linkId}` - Delete file/folder
- `POST /api/drive/shares/{shareId}/files` - Upload file
- `GET /api/drive/shares/{shareId}/files/{fileId}` - Download file

## Data Models

Key data structures to implement:

```kotlin
data class Volume(
    val id: String,
    val name: String,
    val maxSpace: Long,
    val usedSpace: Long
)

data class Share(
    val id: String,
    val volumeId: String,
    val linkId: String,
    val type: ShareType
)

data class Link(
    val id: String,
    val shareId: String,
    val parentLinkId: String?,
    val type: LinkType,
    val name: String,
    val size: Long,
    val mimeType: String,
    val hash: String,
    val modifiedTime: Long
)

enum class LinkType {
    FILE,
    FOLDER
}
```

## Next Steps

1. Review Proton Drive SDK source code
2. Determine if SDK supports JVM/Desktop
3. If not, create API client from scratch
4. Implement authentication flow
5. Implement file upload/download
6. Add encryption/decryption
7. Build sync engine on top

## Security Considerations

- **Zero-Access Encryption**: All files encrypted client-side
- **Secure Key Storage**: Use platform keychain when available
- **TLS**: All API calls over HTTPS
- **Session Security**: Secure token storage and rotation

## Testing

- Unit tests for API client
- Integration tests with Proton Drive sandbox
- End-to-end sync tests
- Security audit of crypto implementation
