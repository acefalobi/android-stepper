@file:Suppress("KDocMissingDocumentation")

object PluginDependencies {
    const val ANDROID_MAVEN = "com.github.dcendents.android-maven"

    const val NAVIGATION = "androidx.navigation.safeargs.kotlin"
    const val SPOTLESS = "plugins.spotless"
}

object ProjectDependencies {
    const val stepper = ":stepper"
}

object RootDependencies {

    object Versions {
        const val kotlin = "1.4.0"
    }

    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
}

object AndroidXDependencies {

    object Versions {
        const val appCompat = "1.2.0"
        const val constraintLayout = "2.0.0-beta6"
        const val coreKtx = "1.3.0"
        const val navigation = "2.3.0-alpha05"
    }

    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val navigationFragmentKtx =
            "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
}

object AsyncDependencies {

    object Versions {
        const val coroutines = "1.3.9"
    }

    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

}

object UtilityDependencies {

    object Versions {
        const val androidUtils = "master-SNAPSHOT"
    }

    const val androidUtils = "com.github.softcomoss:android-utils:${Versions.androidUtils}"
}

object ViewDependencies {

    object Versions {
        const val materialComponent = "1.1.0"
    }

    const val materialComponent =
            "com.google.android.material:material:${Versions.materialComponent}"
}

object TestingDependencies {

    object Versions {
        const val roboelectric = "4.3"
        const val mockito = "2.25.0"
        const val jUnit = "4.12"
        const val androidJUnit = "1.1.1"
        const val androidTest = "1.1.0"
        const val androidTestRunner = "1.1.1"
    }
    const val jUnit = "junit:junit:${Versions.jUnit}"
    const val androidJUnit = "androidx.test.ext:junit:${Versions.androidJUnit}"
    const val androidTestRunner = "androidx.test:runner:${Versions.androidTestRunner}"
    const val androidTest = "androidx.test:core:${Versions.androidTest}"
    const val mockitoCore = "org.mockito:mockito-core:${Versions.mockito}"
    const val roboelectric = "org.robolectric:robolectric:${Versions.roboelectric}"
}

object ClasspathDependencies {

    object Versions {
        const val androidMaven = "2.1"
        const val gradle = "4.0.1"
        const val hilt = "2.28-alpha"
        const val navigation = "2.1.0"
        const val spotless = "4.3.0"
    }

    const val allopen = "org.jetbrains.kotlin:kotlin-allopen:${RootDependencies.Versions.kotlin}"
    const val androidMaven = "com.github.dcendents:android-maven-gradle-plugin:${Versions.androidMaven}"
    const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
    const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    const val navigation =
            "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val spotless = "com.diffplug.spotless:spotless-plugin-gradle:${Versions.spotless}"
}