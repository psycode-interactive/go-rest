import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.psycodeinteractive.gorest.feature.users.testutils"
    compileSdk = libs.versions.appCompileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.appMinSdk.get().toInt()
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
                        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    )
                )
            }
        }
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.presentation)
    implementation(projects.feature.users.usersData)
    implementation(projects.feature.users.usersDomain)
    implementation(projects.feature.users.usersPresentation)

    api(libs.test.junit)
    api(libs.test.junit.kotlin)
    api(libs.test.coroutines)
    api(libs.test.android)
    api(libs.test.android.ktx)
    api(libs.kotest.assertions)
}
