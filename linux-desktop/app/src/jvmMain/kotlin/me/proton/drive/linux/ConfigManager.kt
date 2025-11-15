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

package me.proton.drive.linux

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Properties

/**
 * Configuration manager for Proton Drive Linux
 * Handles application settings and preferences
 */
class ConfigManager {
    
    private val configDir: Path = Paths.get(System.getProperty("user.home"), ".config", "proton-drive")
    private val configFile: Path = configDir.resolve("config.properties")
    private val properties = Properties()
    
    init {
        // Create config directory if it doesn't exist
        if (!Files.exists(configDir)) {
            Files.createDirectories(configDir)
        }
        
        // Load existing config or create default
        if (Files.exists(configFile)) {
            Files.newInputStream(configFile).use { input ->
                properties.load(input)
            }
        } else {
            setDefaults()
            save()
        }
    }
    
    private fun setDefaults() {
        properties.setProperty("sync.folder", Paths.get(System.getProperty("user.home"), "ProtonDrive").toString())
        properties.setProperty("sync.auto_start", "true")
        properties.setProperty("sync.interval_seconds", "300")
        properties.setProperty("ui.minimize_to_tray", "true")
        properties.setProperty("ui.show_notifications", "true")
    }
    
    fun getSyncFolder(): Path {
        return Paths.get(properties.getProperty("sync.folder"))
    }
    
    fun setSyncFolder(path: Path) {
        properties.setProperty("sync.folder", path.toString())
        save()
    }
    
    fun getAutoStart(): Boolean {
        return properties.getProperty("sync.auto_start", "true").toBoolean()
    }
    
    fun setAutoStart(enabled: Boolean) {
        properties.setProperty("sync.auto_start", enabled.toString())
        save()
    }
    
    fun getSyncInterval(): Int {
        return properties.getProperty("sync.interval_seconds", "300").toInt()
    }
    
    fun setSyncInterval(seconds: Int) {
        properties.setProperty("sync.interval_seconds", seconds.toString())
        save()
    }
    
    fun getMinimizeToTray(): Boolean {
        return properties.getProperty("ui.minimize_to_tray", "true").toBoolean()
    }
    
    fun setMinimizeToTray(enabled: Boolean) {
        properties.setProperty("ui.minimize_to_tray", enabled.toString())
        save()
    }
    
    fun getShowNotifications(): Boolean {
        return properties.getProperty("ui.show_notifications", "true").toBoolean()
    }
    
    fun setShowNotifications(enabled: Boolean) {
        properties.setProperty("ui.show_notifications", enabled.toString())
        save()
    }
    
    private fun save() {
        Files.newOutputStream(configFile).use { output ->
            properties.store(output, "Proton Drive Configuration")
        }
    }
    
    fun getDatabasePath(): Path {
        return configDir.resolve("proton-drive.db")
    }
}
