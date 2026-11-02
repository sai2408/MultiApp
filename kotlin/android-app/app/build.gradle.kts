plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.multiapp.tasks"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.multiapp.tasks"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // 10.0.2.2 is the emulator's alias for your PC. 8083 = Ktor; use any backend port from the root README.
        buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:8083/\"")
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    debugImplementation("androidx.compose.ui:ui-tooling")
}
