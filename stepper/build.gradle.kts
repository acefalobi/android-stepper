import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("plugin.allopen")
    kotlin("kapt")
    id(PluginDependencies.ANDROID_MAVEN)
}

group = "com.github.acefalobi"

android {
    compileSdkVersion(BuildConfiguration.compileSdkVersion)

    defaultConfig {
        minSdkVersion(BuildConfiguration.minSdkVersion)
        targetSdkVersion(BuildConfiguration.targetSdkVersion)

        versionCode = BuildConfiguration.versionCode
        versionName = BuildConfiguration.versionName

        setProperty("archivesBaseName", "android-stepper-$versionName")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    androidExtensions {
        isExperimental = true
    }

    lintOptions {
        isCheckAllWarnings = true
        isWarningsAsErrors = true
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(RootDependencies.kotlin)

    implementation(AndroidXDependencies.appCompat)
    implementation(AndroidXDependencies.constraintLayout)
    implementation(AndroidXDependencies.coreKtx)
    implementation(AndroidXDependencies.navigationFragmentKtx)
    implementation(AndroidXDependencies.navigationUiKtx)

    implementation(UtilityDependencies.androidUtils)

    implementation(ViewDependencies.materialComponent)

    testImplementation(TestingDependencies.androidTest)
    testImplementation(TestingDependencies.jUnit)
    testImplementation(TestingDependencies.mockitoCore)
    testImplementation(TestingDependencies.roboelectric)

    androidTestImplementation(TestingDependencies.androidJUnit)
    androidTestImplementation(TestingDependencies.androidTestRunner)
}
