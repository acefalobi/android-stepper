buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", RootDependencies.Versions.kotlin))
        classpath(ClasspathDependencies.allopen)
        classpath(ClasspathDependencies.androidMaven)
        classpath(ClasspathDependencies.gradle)
        classpath(ClasspathDependencies.hilt)
        classpath(ClasspathDependencies.navigation)
        classpath(ClasspathDependencies.spotless)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
    }

    plugins.apply(PluginDependencies.SPOTLESS)

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}
