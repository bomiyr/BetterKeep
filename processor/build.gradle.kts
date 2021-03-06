plugins {
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(project(":annotations"))
    implementation(project(":rulesgenerator"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.6.10-1.0.2")
}