plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.airqualityprojectfinish"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.airqualityprojectfinish"
        minSdk = 24
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.squareup.retrofit2:retrofit:2.1.0")
    implementation ("com.google.code.gson:gson:2.6.2")
    implementation ("com.squareup.retrofit2:converter-gson:2.1.0")
    implementation ("org.jsoup:jsoup:1.10.3")
    implementation ("com.squareup.okhttp3:logging-interceptor:3.4.1")
    testImplementation("junit:junit:4.13.2")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.github.AnyChart:AnyChart-Android:1.1.5")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("org.osmdroid:osmdroid-android:6.1.2")
    implementation("com.google.android.material:material:1.2.0-alpha02")
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation ("com.github.MKergall:osmbonuspack:6.4")

}