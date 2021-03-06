plugins {
    id("gradlebuild.distribution.api-java")
}

description = "Logging infrastructure"

gradlebuildJava.usedInWorkers()

dependencies {
    api(libs.slf4jApi)

    implementation(project(":baseServices"))
    implementation(project(":messaging"))
    implementation(project(":cli"))
    implementation(project(":buildOption"))

    implementation(project(":native"))
    implementation(libs.julToSlf4j)
    implementation(libs.ant)
    implementation(libs.commonsLang)
    implementation(libs.guava)
    implementation(libs.jansi)

    runtimeOnly(libs.log4jToSlf4j)
    runtimeOnly(libs.jclToSlf4j)

    testImplementation(testFixtures(project(":core")))

    integTestImplementation(libs.ansiControlSequenceUtil)

    testFixturesImplementation(project(":baseServices"))
    testFixturesImplementation(testFixtures(project(":core")))
    testFixturesImplementation(libs.slf4jApi)

    integTestDistributionRuntimeOnly(project(":distributionsCore"))
}

classycle {
    excludePatterns.set(listOf("org/gradle/internal/featurelifecycle/**"))
}
