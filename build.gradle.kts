plugins {
    application
    java
    id("org.openjfx.javafxplugin") version "0.0.14"
}

group = "org.example"
version = "1.0-SNAPSHOT"

val javaVersion = 23
val javafxVersion = "24"
val mysqlVersion = "9.3.0"

application {
    mainClass.set("org.example.Main")
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

javafx {
    version = javafxVersion
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.graphics")
}

dependencies {
    // JavaFX (handled by plugin)
    // No need for manual javafx dependencies anymore

    // MySQL
    implementation("com.mysql:mysql-connector-j:$mysqlVersion")

    // Jakarta Mail
    implementation("com.sun.mail:jakarta.mail:2.0.2")
    implementation("com.sun.activation:jakarta.activation:2.0.1")

    // JUnit
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}