val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val ktor_version: String by project

plugins {
    kotlin("jvm") version "2.0.21"
    id("io.ktor.plugin") version "3.0.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
    kotlin("plugin.noarg") version "2.0.20"
}

noArg {
    annotation("coliver.model.NoArg")
}

group = "coliver"
version = "0.0.1"

application {
    mainClass.set("coliver.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-webjars-jvm")
    implementation("org.webjars:jquery:3.2.1")
    implementation("io.ktor:ktor-server-swagger:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposed_version")
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("com.zaxxer:HikariCP:6.0.0")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml-jvm:3.0.0")
    implementation("io.insert-koin:koin-ktor:4.0.0")
    implementation("io.ktor:ktor-client-core-jvm:3.0.0")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-client-apache:3.0.0")
    implementation("io.ktor:ktor-client-apache5:3.0.0")
    implementation("io.ktor:ktor-client-okhttp-jvm:3.0.0")
    implementation("io.ktor:ktor-client-cio-jvm:3.0.0")
    implementation("at.favre.lib:bcrypt:0.10.2")
    implementation("io.ktor:ktor-client-java:$ktor_version")
    implementation("io.github.smiley4:ktor-swagger-ui:4.0.0")
    implementation("io.github.smiley4:schema-kenerator-core:1.5.0")
    implementation("io.github.smiley4:schema-kenerator-serialization:1.5.0")
    implementation("io.github.smiley4:schema-kenerator-swagger:1.5.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("io.minio:minio:8.5.12")
    implementation("io.ktor:ktor-client-jetty:3.0.0")
    implementation("io.ktor:ktor-server-metrics-micrometer:$ktor_version")
    implementation("io.micrometer:micrometer-registry-prometheus:1.13.6")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
