// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.21" apply false
    id ("org.jetbrains.kotlin.kapt") version "2.0.0" apply false
    alias(libs.plugins.compose.compiler) apply false
}
true // Needed to make the Suppress annotation work for the plugins block