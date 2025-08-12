plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp") version "2.0.21-1.0.28"
    alias(libs.plugins.dokka)
    id("maven-publish")
    id("signing")
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
    
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
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

// Publishing configuration
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = findProperty("GROUP")?.toString()
            artifactId = findProperty("POM_ARTIFACT_ID")?.toString()
            version = findProperty("VERSION_NAME")?.toString()
            
            afterEvaluate {
                from(components["release"])
            }
            
            pom {
                name.set(findProperty("POM_NAME")?.toString())
                description.set(findProperty("POM_DESCRIPTION")?.toString())
                url.set(findProperty("POM_URL")?.toString())
                
                licenses {
                    license {
                        name.set(findProperty("POM_LICENSE_NAME")?.toString())
                        url.set(findProperty("POM_LICENSE_URL")?.toString())
                    }
                }
                
                developers {
                    developer {
                        id.set(findProperty("POM_DEVELOPER_ID")?.toString())
                        name.set(findProperty("POM_DEVELOPER_NAME")?.toString())
                        email.set(findProperty("POM_DEVELOPER_EMAIL")?.toString())
                    }
                }
                
                scm {
                    connection.set(findProperty("POM_SCM_CONNECTION")?.toString())
                    developerConnection.set(findProperty("POM_SCM_DEV_CONNECTION")?.toString())
                    url.set(findProperty("POM_SCM_URL")?.toString())
                }
            }
        }
    }
    
    repositories {
        maven {
            name = "centralPortal"
            url = uri("https://central.sonatype.com/api/v1/publisher/upload?publishingType=AUTOMATIC")
            credentials {
                username = findProperty("SONATYPE_USERNAME")?.toString() ?: System.getenv("SONATYPE_USERNAME")
                password = findProperty("SONATYPE_PASSWORD")?.toString() ?: System.getenv("SONATYPE_PASSWORD")
            }
        }
    }
}

signing {
    val signingKeyId = findProperty("signing.keyId")?.toString()
    val signingPassword = findProperty("signing.password")?.toString()
    
    if (signingKeyId != null && signingPassword != null) {
        // Use GPG agent for signing instead of reading key file
        sign(publishing.publications["release"])
    }
}

tasks.dokkaHtml.configure {
    outputDirectory.set(layout.buildDirectory.dir("dokka"))
}