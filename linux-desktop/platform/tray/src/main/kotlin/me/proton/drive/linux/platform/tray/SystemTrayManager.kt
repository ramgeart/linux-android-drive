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

package me.proton.drive.linux.platform.tray

import java.awt.*
import java.awt.event.ActionListener
import javax.swing.ImageIcon

/**
 * System tray integration for Linux
 * Provides quick access to Proton Drive features from the system tray
 */
class SystemTrayManager {
    
    private var tray: SystemTray? = null
    private var trayIcon: TrayIcon? = null
    
    fun initialize(
        onOpen: () -> Unit,
        onSync: () -> Unit,
        onPause: () -> Unit,
        onSettings: () -> Unit,
        onQuit: () -> Unit
    ) {
        if (!SystemTray.isSupported()) {
            println("System tray is not supported on this platform")
            return
        }
        
        tray = SystemTray.getSystemTray()
        
        // TODO: Load actual icon
        val image = createDefaultIcon()
        
        val popup = PopupMenu()
        
        // Menu items
        val openItem = MenuItem("Open Proton Drive")
        openItem.addActionListener(ActionListener { onOpen() })
        
        val syncItem = MenuItem("Start Sync")
        syncItem.addActionListener(ActionListener { onSync() })
        
        val pauseItem = MenuItem("Pause Sync")
        pauseItem.addActionListener(ActionListener { onPause() })
        
        popup.add(openItem)
        popup.addSeparator()
        popup.add(syncItem)
        popup.add(pauseItem)
        popup.addSeparator()
        
        val settingsItem = MenuItem("Settings")
        settingsItem.addActionListener(ActionListener { onSettings() })
        popup.add(settingsItem)
        
        popup.addSeparator()
        val quitItem = MenuItem("Quit")
        quitItem.addActionListener(ActionListener { onQuit() })
        popup.add(quitItem)
        
        trayIcon = TrayIcon(image, "Proton Drive", popup).apply {
            isImageAutoSize = true
            addActionListener(ActionListener { onOpen() })
        }
        
        try {
            tray?.add(trayIcon)
        } catch (e: AWTException) {
            println("Failed to add system tray icon: ${e.message}")
        }
    }
    
    fun updateStatus(status: TrayStatus) {
        trayIcon?.toolTip = when (status) {
            TrayStatus.IDLE -> "Proton Drive - Idle"
            TrayStatus.SYNCING -> "Proton Drive - Syncing..."
            TrayStatus.PAUSED -> "Proton Drive - Paused"
            TrayStatus.ERROR -> "Proton Drive - Error"
        }
    }
    
    fun showNotification(title: String, message: String, type: TrayIcon.MessageType = TrayIcon.MessageType.INFO) {
        trayIcon?.displayMessage(title, message, type)
    }
    
    fun remove() {
        trayIcon?.let { icon ->
            tray?.remove(icon)
        }
    }
    
    private fun createDefaultIcon(): Image {
        // Create a simple colored square as placeholder
        val size = 16
        val bufferedImage = java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB)
        val g = bufferedImage.createGraphics()
        
        // Draw a purple square (Proton brand color)
        g.color = Color(0x6D, 0x4A, 0xFF)
        g.fillRect(0, 0, size, size)
        
        g.dispose()
        return bufferedImage
    }
}

enum class TrayStatus {
    IDLE,
    SYNCING,
    PAUSED,
    ERROR
}
