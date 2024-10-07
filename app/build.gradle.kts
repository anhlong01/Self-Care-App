plugins {
    alias(libs.plugins.android.application)
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

    implementation ("androidx.fragment:fragment:1.3.6")

}