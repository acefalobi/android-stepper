import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("plugin.allopen")
    kotlin("kapt")
    id(PluginDependencies.NAVIGATION)
}

android {
    compileSdkVersion(BuildConfiguration.compileSdkVersion)

    defaultConfig {
        applicationId = BuildConfiguration.applicationId
        minSdkVersion(BuildConfiguration.minSdkVersion)
        targetSdkVersion(BuildConfiguration.targetSdkVersion)

        versionCode = BuildConfiguration.versionCode
        versionName = BuildConfiguration.versionName

        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        setProperty("archivesBaseName", "$applicationId-app-$versionName")
    }

    @Suppress("UnstableApiUsage")
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    androidExtensions {
        isExperimental = true
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(RootDependencies.kotlin)

    implementation(project(ProjectDependencies.stepper))

    implementation(AndroidXDependencies.appCompat)
    implementation(AndroidXDependencies.constraintLayout)
    implementation(AndroidXDependencies.coreKtx)
    implementation(AndroidXDependencies.navigationFragmentKtx)
    implementation(AndroidXDependencies.navigationUiKtx)

    implementation(ViewDependencies.materialComponent)

    implementation(AsyncDependencies.coroutines)
    implementation(AsyncDependencies.coroutinesAndroid)

    implementation(UtilityDependencies.androidUtils)

    testImplementation(TestingDependencies.jUnit)
    testImplementation(TestingDependencies.mockitoCore)
    testImplementation(TestingDependencies.roboelectric)
    androidTestImplementation(TestingDependencies.androidJUnit)
    testImplementation(TestingDependencies.androidTest)
    androidTestImplementation(TestingDependencies.androidTestRunner)
}
