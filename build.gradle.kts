plugins {
    application
    java
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

val osName = System.getProperty("os.name").lowercase()
val javafxPlatform = when {
    osName.contains("win") -> "win"
    osName.contains("mac") -> "mac"
    else -> "linux"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

dependencies {
    implementation("org.openjfx:javafx-base:$javafxVersion:$javafxPlatform")
    implementation("org.openjfx:javafx-controls:$javafxVersion:$javafxPlatform")
    implementation("org.openjfx:javafx-fxml:$javafxVersion:$javafxPlatform")
    implementation("org.openjfx:javafx-graphics:$javafxVersion:$javafxPlatform")

    implementation("com.mysql:mysql-connector-j:$mysqlVersion")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<JavaExec>().configureEach {
    val javafxJarsList = classpath.filter { it.name.contains("javafx") }.toList()
    if (javafxJarsList.isNotEmpty()) {
        val modulePath = javafxJarsList.joinToString(separator = File.pathSeparator) { it.absolutePath }
        jvmArgs = listOf(
            "--module-path", modulePath,
            "--add-modules", "javafx.controls,javafx.fxml"
        )
    }
}

tasks.test {
    useJUnitPlatform()
}
