plugins {
    kotlin("jvm") version "1.9.21"
    war
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("javax.xml.ws:jaxws-api:2.3.1")
    implementation("com.sun.xml.ws:jaxws-ri:2.3.2")
    implementation("javax.jws:javax.jws-api:1.1")
    implementation("org.postgresql:postgresql:42.2.18")
}

tasks.test {
    useJUnitPlatform()
}

tasks.war {
    archiveFileName.set("my_j2ee_app.war")
}

kotlin {
    jvmToolchain(11)
}