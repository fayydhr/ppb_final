// fayydhr/ppb_final/ppb_final-04d3009cc54b382f7d143063b28dbba9ab4b4681/GymBuddy/settings.gradle.kts
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // Ensure this line is present
    }
}
rootProject.name = "GymBuddy"
include(":app")