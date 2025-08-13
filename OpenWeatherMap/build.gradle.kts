import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp") version "2.0.21-1.0.28"
    alias(libs.plugins.dokka)
    id("com.vanniktech.maven.publish")
    id("com.gradleup.nmcp")
    signing
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

// Explicit GPG signing configuration using gpg command
signing {
    useGpgCmd()
    sign(publishing.publications)
}

// Configure Maven publishing with Central Portal
mavenPublishing {
    // Use Maven Central Portal for publishing
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)
    
    // Enable signing for all publications
    signAllPublications()
    
    // Configure what to publish for Android library
    configure(
        AndroidSingleVariantLibrary(
            variant = "release",
            sourcesJar = true,
            publishJavadocJar = true
        )
    )
    
    // Configure POM details (will read from gradle.properties but can be overridden)
    pom {
        name.set("OpenWeatherMap Android SDK")
        description.set("A comprehensive Android SDK for OpenWeatherMap API with full feature coverage including current weather, forecasts, air pollution, geocoding, and weather maps")
        inceptionYear.set("2025")
        url.set("https://github.com/idineshgovind/OpenWeatherMap-AndroidSDK")
        
        licenses {
            license {
                name.set("The Apache Software License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        
        developers {
            developer {
                id.set("idineshgovind")
                name.set("Dinesh G")
                url.set("https://github.com/idineshgovind")
            }
        }
        
        scm {
            url.set("https://github.com/idineshgovind/OpenWeatherMap-AndroidSDK")
            connection.set("scm:git:git://github.com/idineshgovind/OpenWeatherMap-AndroidSDK.git")
            developerConnection.set("scm:git:ssh://git@github.com/idineshgovind/OpenWeatherMap-AndroidSDK.git")
        }
    }
}

tasks.dokkaHtml.configure {
    outputDirectory.set(layout.buildDirectory.dir("dokka"))
}