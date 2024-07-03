/*
 * MIT License
 *
 * Copyright (c) 2023 Radzivon Bartoshyk
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

@file:Suppress("UnstableApiUsage")

import com.android.tools.r8.internal.pw
import java.net.URI


plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

task("androidSourcesJar", Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                groupId = "com.github.awxkee"
                artifactId = "avif-coder"
                version = "2.0.1"
                from(components["release"])
//                artifact("androidSourcesJar")
            }
        }
        repositories{
            maven{
                url = URI("https://nexus.hss01248.tech/repository/maven-releases/")
                credentials {
                    username ="xx"
                    password ="xx"
                }
            }
        }
    }
}

android {
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

    namespace = "com.github.awxkee.avifcoder"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        externalNativeBuild {
            cmake {
                ndkVersion = "26.1.10909125"
                cppFlags.addAll(
                    listOf(
                        "-std=c++20",
                    )
                )
                abiFilters += setOf("armeabi-v7a", "arm64-v8a", "x86_64", "x86")
            }
        }

    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("src/main/jniLibs")
        }
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            consumerProguardFiles("consumer-rules.pro")
        }
    }
    externalNativeBuild {
        cmake {
            version = "3.22.1"
            path("src/main/cpp/CMakeLists.txt")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}
dependencies {
    implementation("androidx.annotation:annotation:1.2.0")
}
