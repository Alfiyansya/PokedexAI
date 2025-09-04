// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript{
    extra.set("compose_version", "1.0.0-beta01")
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.room) apply false
}