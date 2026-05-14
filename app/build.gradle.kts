plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.app.granineaapp"
    compileSdk = 36 // Nota: Bajé a 34 para estabilidad, ya que 36 es preliminar

    defaultConfig {
        applicationId = "com.app.granineaapp"
        minSdk = 31
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // --- NUEVAS DEPENDENCIAS PARA HUELLA Y GOOGLE ---
    implementation("androidx.biometric:biometric:1.1.0")
    implementation("com.google.android.gms:play-services-auth:21.1.1")
    // ------------------------------------------------

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}