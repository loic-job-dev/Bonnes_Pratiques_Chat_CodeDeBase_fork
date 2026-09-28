plugins {
    id("java")
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("commons-io:commons-io:2.22.0")
    implementation("org.apache.commons:commons-lang3:3.20.0")
    implementation("com.google.guava:guava:33.7.1-jre")
    implementation("org.json:json:20260814")
    implementation("log4j:log4j:1.2.17")
    implementation("joda-time:joda-time:2.14.3")
    implementation("ch.qos.logback:logback-classic:1.6.4")

    testImplementation("org.junit.jupiter:junit-jupiter:5.13.4")
    testImplementation("org.mockito:mockito-junit-jupiter:5.19.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.13.4")
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainClass.set("org.example.Main")
}
