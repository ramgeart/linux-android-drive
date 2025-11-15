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

package me.proton.drive.linux.database

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.Path

/**
 * Database manager for Proton Drive Linux
 * Stores sync metadata, file states, and configuration
 */
class DatabaseManager(dbPath: Path) {
    
    init {
        Database.connect("jdbc:sqlite:${dbPath.toAbsolutePath()}", "org.sqlite.JDBC")
        
        transaction {
            SchemaUtils.create(
                Files,
                SyncState,
                Settings
            )
        }
    }
    
    fun insertFile(
        linkId: String,
        localPath: String,
        remoteHash: String,
        size: Long,
        modifiedTime: Long
    ) = transaction {
        Files.insert {
            it[Files.linkId] = linkId
            it[Files.localPath] = localPath
            it[Files.remoteHash] = remoteHash
            it[Files.fileSize] = size
            it[Files.modifiedTime] = modifiedTime
            it[Files.syncStatus] = SyncStatus.SYNCED.name
        }
    }
    
    fun getFileByLinkId(linkId: String): FileMetadata? = transaction {
        Files.selectAll().where { Files.linkId eq linkId }
            .map { row ->
                FileMetadata(
                    linkId = row[Files.linkId],
                    localPath = row[Files.localPath],
                    remoteHash = row[Files.remoteHash],
                    fileSize = row[Files.fileSize],
                    modifiedTime = row[Files.modifiedTime],
                    syncStatus = SyncStatus.valueOf(row[Files.syncStatus])
                )
            }
            .singleOrNull()
    }
    
    fun getAllFiles(): List<FileMetadata> = transaction {
        Files.selectAll().map { row ->
            FileMetadata(
                linkId = row[Files.linkId],
                localPath = row[Files.localPath],
                remoteHash = row[Files.remoteHash],
                fileSize = row[Files.fileSize],
                modifiedTime = row[Files.modifiedTime],
                syncStatus = SyncStatus.valueOf(row[Files.syncStatus])
            )
        }
    }
    
    fun updateSyncStatus(linkId: String, status: SyncStatus) = transaction {
        Files.update({ Files.linkId eq linkId }) {
            it[syncStatus] = status.name
        }
    }
    
    fun deleteFile(linkId: String) = transaction {
        Files.deleteWhere { Files.linkId eq linkId }
    }
}

object Files : LongIdTable("files") {
    val linkId = varchar("link_id", 255).uniqueIndex()
    val localPath = varchar("local_path", 4096)
    val remoteHash = varchar("remote_hash", 255)
    val fileSize = long("file_size")
    val modifiedTime = long("modified_time")
    val syncStatus = varchar("sync_status", 50)
}

object SyncState : LongIdTable("sync_state") {
    val key = varchar("key", 255).uniqueIndex()
    val value = text("value")
}

object Settings : LongIdTable("settings") {
    val key = varchar("key", 255).uniqueIndex()
    val value = text("value")
}

data class FileMetadata(
    val linkId: String,
    val localPath: String,
    val remoteHash: String,
    val fileSize: Long,
    val modifiedTime: Long,
    val syncStatus: SyncStatus
)

enum class SyncStatus {
    PENDING_UPLOAD,
    PENDING_DOWNLOAD,
    SYNCED,
    CONFLICT,
    ERROR
}
