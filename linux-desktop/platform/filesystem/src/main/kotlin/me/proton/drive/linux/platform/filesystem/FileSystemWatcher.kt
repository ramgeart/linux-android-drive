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

package me.proton.drive.linux.platform.filesystem

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

/**
 * Monitors file system changes using Java WatchService
 */
class FileSystemWatcher(
    private val watchPath: Path
) {
    private val watchService: WatchService = FileSystems.getDefault().newWatchService()
    
    init {
        registerPath(watchPath)
    }
    
    private fun registerPath(path: Path) {
        path.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY
        )
        
        // Register subdirectories recursively
        Files.walkFileTree(path, object : SimpleFileVisitor<Path>() {
            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                if (dir != path) {
                    dir.register(
                        watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY
                    )
                }
                return FileVisitResult.CONTINUE
            }
        })
    }
    
    fun watchEvents(): Flow<FileSystemEvent> = flow {
        while (true) {
            val key = watchService.take()
            
            for (event in key.pollEvents()) {
                val kind = event.kind()
                
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue
                }
                
                @Suppress("UNCHECKED_CAST")
                val ev = event as WatchEvent<Path>
                val filename = ev.context()
                val dir = key.watchable() as Path
                val child = dir.resolve(filename)
                
                val fileSystemEvent = when (kind) {
                    StandardWatchEventKinds.ENTRY_CREATE -> {
                        // If a new directory is created, register it for watching
                        if (Files.isDirectory(child)) {
                            registerPath(child)
                        }
                        FileSystemEvent.Created(child)
                    }
                    StandardWatchEventKinds.ENTRY_DELETE -> FileSystemEvent.Deleted(child)
                    StandardWatchEventKinds.ENTRY_MODIFY -> FileSystemEvent.Modified(child)
                    else -> continue
                }
                
                emit(fileSystemEvent)
            }
            
            val valid = key.reset()
            if (!valid) {
                break
            }
        }
    }
    
    fun close() {
        watchService.close()
    }
}

sealed class FileSystemEvent {
    abstract val path: Path
    
    data class Created(override val path: Path) : FileSystemEvent()
    data class Modified(override val path: Path) : FileSystemEvent()
    data class Deleted(override val path: Path) : FileSystemEvent()
}
