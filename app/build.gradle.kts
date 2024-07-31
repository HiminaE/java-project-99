plugins {
	java
	id("org.springframework.boot") version "3.2.8-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.6"
	checkstyle
	application
	id("io.freefair.lombok") version "8.6"
	jacoco
}

application {
    applicationName = "app"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter:3.3.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.8")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.0")
	implementation("org.springframework.data:spring-data-jpa:3.1.3")
	implementation("org.springframework.boot:spring-boot-starter-web:3.3.1")
	implementation("org.springframework.boot:spring-boot-starter-security:3.3.1")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.3.1")
	testImplementation("org.springframework.security:spring-security-test:6.2.4")

	runtimeOnly("com.h2database:h2:2.2.222")
	implementation("org.postgresql:postgresql:42.7.3")

	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:3.3.0")

	implementation("org.mapstruct:mapstruct:1.6.0.Beta2")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")

	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
	implementation("net.datafaker:datafaker:2.0.2")
	implementation("org.instancio:instancio-junit:3.3.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

application {
	mainClass = "hexlet.code.app.AppApplication"
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required = true
	}
}
