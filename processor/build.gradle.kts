plugins {
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":annotations"))
    implementation(project(":rulesgenerator"))
    implementation(libs.kotlin.symbol.processing)
}