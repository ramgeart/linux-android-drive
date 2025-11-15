/*
 * Copyright (c) 2025 Proton AG.
 * This file is part of Proton Drive.
 *
 * Proton Drive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Proton Drive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Proton Drive.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.proton.drive.linux.platform.notifications

/**
 * Desktop notification service for Linux
 * Uses D-Bus to send notifications via org.freedesktop.Notifications
 */
class NotificationService {
    
    fun showNotification(
        title: String,
        message: String,
        urgency: NotificationUrgency = NotificationUrgency.NORMAL
    ) {
        // TODO: Implement D-Bus notification
        // For now, just print to console
        println("[NOTIFICATION] $title: $message")
    }
    
    fun showSyncStarted() {
        showNotification(
            "Proton Drive",
            "Sync started"
        )
    }
    
    fun showSyncCompleted(filesCount: Int) {
        showNotification(
            "Proton Drive",
            "Sync completed: $filesCount files synced"
        )
    }
    
    fun showSyncError(error: String) {
        showNotification(
            "Proton Drive - Error",
            "Sync failed: $error",
            NotificationUrgency.CRITICAL
        )
    }
    
    fun showUploadComplete(fileName: String) {
        showNotification(
            "Proton Drive",
            "Upload complete: $fileName"
        )
    }
    
    fun showDownloadComplete(fileName: String) {
        showNotification(
            "Proton Drive",
            "Download complete: $fileName"
        )
    }
}

enum class NotificationUrgency {
    LOW,
    NORMAL,
    CRITICAL
}
