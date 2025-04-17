import org.gradle.internal.impldep.com.google.api.client.util.Data.mapOf

plugins {
    java
    id("idea")
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.7"

    id("com.github.johnrengelman.shadow") version "7.1.1"
}

group = "org.dash"
version = "1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        sourceCompatibility = JavaVersion.VERSION_17
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.java-websocket:Java-WebSocket:1.6.0")
    implementation("org.json:json:20250107")
    implementation("org.hibernate.orm:hibernate-core:6.6.12.Final")
    implementation("com.zaxxer:HikariCP:6.3.0")
    implementation("org.postgresql:postgresql:42.7.5")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
