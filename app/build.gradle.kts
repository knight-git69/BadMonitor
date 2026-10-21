plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.homemonitor"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.homemonitor"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // ⚠️ গিটহাব সিক্রেট থেকে ডেটা নেওয়ার জন্য
        buildConfigField("String", "TELEGRAM_TOKEN", "\"${System.getenv("TELEGRAM_TOKEN") ?: ""}\"")
        buildConfigField("String", "TELEGRAM_CHAT_ID", "\"${System.getenv("TELEGRAM_CHAT_ID") ?: ""}\"")
    }

    buildTypes {
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
    }
    
    // ⚠️ BuildConfig জেনারেট করার জন্য
    buildFeatures {
        viewBinding = true
        buildConfig = true 
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)

    implementation(libs.camera.core)
    implementation(libs.camera.camera2)
    implementation(libs.camera.lifecycle)

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    implementation(libs.coroutines.android)
    implementation(libs.lifecycle.service)
}