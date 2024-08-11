import java.time.ZonedDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.psycodeinteractive.gorest"
    compileSdk = libs.versions.appCompileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.psycodeinteractive.gorest"
        minSdk = libs.versions.appMinSdk.get().toInt()
        targetSdk = libs.versions.appTargetSdk.get().toInt()
        versionCode = generateVersionCode()
        versionName = generateVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions {
                freeCompilerArgs.addAll(
                    listOf(
                        "-opt-in=com.google.accompanist.permissions.ExperimentalPermissionsApi",
                    )
                )
            }
        }
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("/META-INF/LICENSE*")
            excludes.add("META-INF/gradle/incremental.annotation.processors")
        }
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.presentation)
    implementation(projects.feature.users.usersData)
    implementation(projects.feature.users.usersDomain)
    implementation(projects.feature.users.usersPresentation)

    implementation(libs.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.activity.compose)
    implementation(libs.timber)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.compiler.android)

    implementation(libs.accompanist.permissions)
    implementation(libs.leakcanary)
}

fun generateVersionName(): String {
    return libs.versions.run { "${appVersionMajor.get()}.${appVersionMinor.get()}.${appVersionPatch.get()}" }
}

fun generateVersionCode(): Int {
    return DateTimeFormatter.ofPattern("yyyyMMddHHmm")
        .format(ZonedDateTime.now(ZoneOffset.UTC))
        .run {
            substring(2, lastIndex).toInt()
        }
}
