plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.lph.selfcareapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lph.selfcareapp"
        minSdk = 24
        targetSdk = 34
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

    buildFeatures{
        dataBinding = true
        viewBinding = true;
    }
}

dependencies {
    implementation("com.github.miteshpithadiya:SearchableSpinner:master")
    //navigation
    implementation ("com.github.ittianyu:BottomNavigationViewEx:1.2.4")
    // Navigation component
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")

//    retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.11.0");
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    implementation("com.android.volley:volley:1.2.1")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.android.material:material:1.12.0")
    //    glide
    implementation ("com.github.bumptech.glide:glide:4.14.2")
   // Skip this if you don't want to use integration libraries or configure Glide.
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")
    implementation("com.applandeo:material-calendar-view:1.9.2")

    implementation ("io.github.chaosleung:pinview:1.4.4")

    implementation ("androidx.fragment:fragment:1.3.6")
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")
//    stringee
    implementation ("com.stringee.sdk.android:stringee-android-sdk:2.1.2")



    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))

    // When using the BoM, you don't specify versions in Firebase library dependencies

    // Add the dependency for the Firebase SDK for Google Analytics
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-messaging")
    implementation ("com.google.android.gms:play-services-maps:17.0.0")
    implementation ("com.google.android.gms:play-services-location:17.0.0")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation ("androidx.biometric:biometric:1.1.0")
    implementation ("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("androidx.paging:paging-runtime:3.1.1")
    implementation("androidx.paging:paging-rxjava3:3.1.1")
    // RxJava3 with retrofit
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")
    implementation("androidx.work:work-runtime:2.9.0")
    // https://mvnrepository.com/artifact/com.github.mhiew/android-pdf-viewer
    implementation("com.github.mhiew:android-pdf-viewer:3.2.0-beta.3")




}
