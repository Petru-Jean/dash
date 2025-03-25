plugins {
    id("java")
}

group = "org.dash"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    // https://mvnrepository.com/artifact/org.java-websocket/Java-WebSocket
    implementation("org.java-websocket:Java-WebSocket:1.6.0")
    implementation("org.json:json:20250107")

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
tasks.register<JavaExec>("run") {
    mainClass.set("org.dash.Main") // Your main class
    classpath = sourceSets["main"].runtimeClasspath
}
