plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.dietapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.dietapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.ext.junit)
    implementation(libs.espresso.core)

    // Standard Unit Testing
    testImplementation(libs.junit)

    // Mockito (Use these specific versions)
    testImplementation("org.mockito:mockito-core:5.11.0")

    // Instrumented testing (for real devices)
    // ...to testImplementation
    testImplementation(libs.ext.junit)
    testImplementation(libs.espresso.core)

}