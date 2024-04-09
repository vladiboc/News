plugins {
	java
	id("org.springframework.boot") version "3.2.3"
	id("io.spring.dependency-management") version "1.1.4"
	id("io.freefair.lombok") version "8.6"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("org.postgresql:postgresql")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.4.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("net.javacrumbs.json-unit:json-unit:2.38.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
