import java.util.regex.Pattern.compile

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")


}

android {
    namespace = "com.example.pfemini"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pfemini"
        minSdk = 29
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation ("com.google.zxing:core:3.4.1")
    implementation ("com.journeyapps:zxing-android-embedded:4.2.0")
    implementation("androidx.annotation:annotation:1.7.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("org.osmdroid:osmdroid-android:6.1.14")
    implementation ("com.itextpdf:itextg:5.5.10")
    implementation ("com.ramotion.circlemenu:circle-menu:0.3.2")
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.28")
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")
    implementation("com.mapbox.maps:android:11.3.1")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-messaging:24.0.0")
    testImplementation("junit:junit:4.13.2")


    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}