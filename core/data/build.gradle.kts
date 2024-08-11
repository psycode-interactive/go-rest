import java.util.Properties
import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.invoke

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.psycodeinteractive.gorest.core.data"
    compileSdk = libs.versions.appCompileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.appMinSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
        }
        debug {
            buildConfigField("String", "GO_REST_API_KEY", localProperties.getOrDefault("GO_REST_API_KEY", "") as String)
        }
        release {
            buildConfigField("String", "GO_REST_API_KEY", localProperties.getOrDefault("GO_REST_API_KEY", "") as String)
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
    }
}

dependencies {
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.json)
    implementation(libs.kotlin.serialization.json)
    debugApi(libs.chucker)
    releaseApi(libs.chucker.no.op)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.compiler.android)
}
