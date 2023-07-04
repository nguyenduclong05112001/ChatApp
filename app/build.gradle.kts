plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    //firebase
    id("com.google.gms.google-services")

    //dagger
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.longhrk.mf"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.cwf.app"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(project(":core:dimension"))
    implementation(project(":core:data"))

    implementation("androidx.compose.ui:ui:1.4.3")

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.3.1")
    implementation("androidx.compose.material:material:1.1.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")

    // CameraX
    implementation ("androidx.camera:camera-lifecycle:1.3.0-beta01")
    implementation ("androidx.camera:camera-video:1.3.0-beta01")
    implementation ("androidx.camera:camera-view:1.3.0-beta01")
    implementation ("androidx.camera:camera-extensions:1.3.0-beta01")

    //accompanist permissions
    implementation ("com.google.accompanist:accompanist-permissions:0.28.0")

    //navigation
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    //blurry
    implementation("jp.wasabeef:blurry:4.0.1")

    //constraintlayout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    //glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

    //dagger
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:32.1.1"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:20.5.0")
    implementation("com.google.firebase:firebase-firestore-ktx")

    //coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    //facebook
    implementation("com.facebook.android:facebook-login:latest.release")
    implementation("com.facebook.android:facebook-android-sdk:latest.release")
}