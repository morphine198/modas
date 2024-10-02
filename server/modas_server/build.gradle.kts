plugins {
    kotlin("jvm") version "2.0.20"
    id("io.ktor.plugin") version "2.3.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
}

group = "ru.modas"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Ktor Server
    implementation("io.ktor:ktor-server-core:2.3.0")
    implementation("io.ktor:ktor-server-netty:2.3.0")

    // Работа с базой данных (Exposed)
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")

    // Драйвер базы данных (PostgreSQL)
    implementation("org.postgresql:postgresql:42.5.4")

    // Логирование
    implementation("ch.qos.logback:logback-classic:1.4.5")

    // Kotlinx Serialization (для обработки JSON, скорее всего в конце не нужун будет)
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}