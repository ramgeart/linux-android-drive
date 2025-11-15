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

package me.proton.drive.linux.api

import kotlinx.serialization.Serializable

@Serializable
data class Volume(
    val id: String,
    val name: String,
    val maxSpace: Long,
    val usedSpace: Long,
    val state: VolumeState = VolumeState.ACTIVE
)

@Serializable
enum class VolumeState {
    ACTIVE,
    LOCKED,
    DELETED
}

@Serializable
data class Share(
    val id: String,
    val volumeId: String,
    val linkId: String,
    val type: ShareType,
    val state: ShareState = ShareState.ACTIVE
)

@Serializable
enum class ShareType {
    MAIN,
    STANDARD,
    DEVICE
}

@Serializable
enum class ShareState {
    ACTIVE,
    LOCKED,
    DELETED
}

@Serializable
data class FileLink(
    val id: String,
    val shareId: String,
    val parentLinkId: String?,
    val type: LinkType,
    val name: String,
    val nameHash: String,
    val size: Long,
    val mimeType: String,
    val hash: String,
    val createTime: Long,
    val modifyTime: Long,
    val state: LinkState = LinkState.ACTIVE
)

@Serializable
enum class LinkType {
    FILE,
    FOLDER
}

@Serializable
enum class LinkState {
    DRAFT,
    ACTIVE,
    TRASHED,
    DELETED
}

@Serializable
data class UploadRequest(
    val name: String,
    val hash: String,
    val parentLinkId: String?,
    val mimeType: String,
    val size: Long
)

@Serializable
data class DownloadRequest(
    val linkId: String
)
