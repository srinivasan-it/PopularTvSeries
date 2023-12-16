plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.populartvseries"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.populartvseries"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    val paging_version = "3.1.0"
    val paging_compose_version = "1.0.0-alpha14"
    val room_version = "2.4.1"
    val nav_version = "2.4.0"
    val android_lifecycle = "2.4.0"
    val retrofit_version = "2.9.0"
    val retrofit_logging_version = "5.0.0-alpha.3"
    val hilt_version = "2.44"
    val compose_version = "1.0.5"
    val android_extensions = "2.2.0"
    val koltin_extensions = "1.9.0"
    val nav_compose_version = "2.5.0-alpha01"
    val coil_version = "1.4.0"

    implementation("androidx.core:core-ktx:$koltin_extensions")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.paging:paging-runtime-ktx:$paging_version")
    implementation("androidx.paging:paging-compose:$paging_compose_version")
    implementation("androidx.room:room-paging:$room_version")
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.lifecycle:lifecycle-extensions:$android_extensions")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$android_lifecycle")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$android_lifecycle")
    implementation("androidx.preference:preference:1.2.0")

    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")

    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("com.squareup.okhttp3:logging-interceptor:$retrofit_logging_version")

    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    implementation("androidx.activity:activity-compose:$compose_version")
    implementation("androidx.compose.material:material:$compose_version")
    implementation("androidx.compose.material:material-icons-extended:$compose_version")
    implementation("androidx.compose.animation:animation:$compose_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$android_lifecycle")
    implementation("androidx.navigation:navigation-compose:$nav_compose_version")
    implementation("io.coil-kt:coil-compose:$coil_version")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}