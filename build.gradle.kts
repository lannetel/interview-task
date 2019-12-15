plugins {
    kotlin("jvm") version "1.3.61"
}

val ktorVersion = "1.2.4"
group = "interview"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")

    implementation("org.postgresql:postgresql:42.2.8.jre7")

    implementation("org.slf4j:slf4j-simple:1.7.10")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.10.1")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}