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

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("plugin.serialization")
}

kotlin {
    jvmToolchain(17)
    
    jvm {
        withJava()
    }
    
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
                implementation("io.ktor:ktor-client-core:3.0.1")
                implementation("io.ktor:ktor-client-cio:3.0.1")
                implementation("org.jetbrains.exposed:exposed-core:0.57.0")
                implementation("org.jetbrains.exposed:exposed-dao:0.57.0")
                implementation("org.jetbrains.exposed:exposed-jdbc:0.57.0")
                implementation("org.xerial:sqlite-jdbc:3.47.1.0")
                
                // Platform modules
                implementation(project(":platform:filesystem"))
                implementation(project(":platform:notifications"))
                implementation(project(":platform:tray"))
                implementation(project(":platform:daemon"))
                
                // Core modules
                implementation(project(":sync"))
                implementation(project(":ui"))
                implementation(project(":shared:api"))
                implementation(project(":shared:crypto"))
                implementation(project(":shared:database"))
            }
        }
        
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "me.proton.drive.linux.MainKt"
        
        nativeDistributions {
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Rpm
            )
            packageName = "proton-drive"
            packageVersion = "1.0.0"
            description = "Proton Drive for Linux - Secure cloud storage with end-to-end encryption"
            copyright = "© 2025 Proton AG"
            vendor = "Proton AG"
            
            linux {
                // iconFile.set(project.file("src/jvmMain/resources/icon.png"))
                packageName = "proton-drive"
                debMaintainer = "Proton AG <support@proton.me>"
                menuGroup = "Network"
                appCategory = "Network"
            }
        }
    }
}
