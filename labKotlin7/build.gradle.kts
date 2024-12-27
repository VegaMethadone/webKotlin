plugins {
    kotlin("jvm") version "2.0.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
val ktorVersion = "2.0.1"

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.apache.juddi:juddi-client:3.3.10")
    implementation("com.sun.xml.ws:jaxws-ri:2.3.2") // JAX-WS RI
    implementation("io.ktor:ktor-client-serialization:2.0.0")
    implementation("io.ktor:ktor-client:2.0.0")

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(20)
}