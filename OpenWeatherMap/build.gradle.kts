plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp") version "2.0.21-1.0.28"
    alias(libs.plugins.dokka)
    id("com.vanniktech.maven.publish")
    id("com.gradleup.nmcp")
}

android {
    namespace = "com.dineshdev.openweathermap.sdk"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
        targetSdk = 36

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        
        buildConfigField("String", "SDK_VERSION", "\"1.0.0\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-Xjvm-default=all", "-opt-in=kotlin.RequiresOptIn")
    }
    
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    
    // Networking
    api(libs.retrofit)
    api(libs.retrofit.moshi)
    api(libs.okhttp)
    implementation(libs.okhttp.logging)
    
    // JSON Parsing
    api(libs.moshi)
    api(libs.moshi.kotlin)
    ksp(libs.moshi.codegen)
    
    // Coroutines
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)
    
    // Logging (optional)
    implementation(libs.timber)
    
    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// The vanniktech plugin will handle all publishing configuration automatically
// based on the properties in gradle.properties

tasks.dokkaHtml.configure {
    outputDirectory.set(layout.buildDirectory.dir("dokka"))
}