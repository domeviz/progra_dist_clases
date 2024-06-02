plugins {
    id("java")
}

group = "com.distribuida"

repositories {
    mavenCentral()
}

dependencies {
    implementation( project(":ejemplo01_cdi"))
}

tasks.test {
    useJUnitPlatform()
}