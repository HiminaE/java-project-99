plugins {
	application
	checkstyle
	id("jacoco")
	id("org.springframework.boot") version "3.2.2"
	id("io.spring.dependency-management") version "1.1.4"
	id("io.freefair.lombok") version "8.6"
	id("io.sentry.jvm.gradle") version "4.8.0"
}
application {mainClass.set("hexlet.code.AppApplication")}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter:3.3.2")
	implementation("org.springframework.boot:spring-boot-starter-web:3.3.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.1")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.2")
	runtimeOnly("com.h2database:h2:2.2.224")
	runtimeOnly("org.postgresql:postgresql:42.7.3")

	implementation("net.datafaker:datafaker:2.3.0")
	implementation("org.instancio:instancio-junit:5.0.1")
	testImplementation(platform("org.junit:junit-bom:5.10.3"))
	testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.7")

	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-security")

	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")

	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	testImplementation("org.springframework.security:spring-security-test")
	implementation("org.springframework.boot:spring-boot-devtools")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
}

sentry {

	includeSourceContext = true

	org = "personal-use-jw"
	projectName = "java-spring-boot"
	authToken = System.getenv("SENTRY_AUTH_TOKEN")
}

tasks.test {
	useJUnitPlatform()
}

tasks.jacocoTestReport {
	reports {
		xml.required.set(true)
	}
}

tasks.sentryBundleSourcesJava {
	enabled = System.getenv("SENTRY_AUTH_TOKEN") != null
}