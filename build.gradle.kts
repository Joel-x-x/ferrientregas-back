plugins {
    java
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.ferrientregas"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("com.mysql:mysql-connector-j")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
//    https://mvnrepository.com/artifact/com.auth0/java-jwt
//    implementation("com.auth0:java-jwt:4.5.0")
    implementation ("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly ("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly ("io.jsonwebtoken:jjwt-jackson:0.12.6")
    // https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")
    // https://mvnrepository.com/artifact/jakarta.inject/jakarta.inject-api
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")
    // https://mvnrepository.com/artifact/javax.mail/mail
    implementation("javax.mail:mail:1.4.7")
    // https://mvnrepository.com/artifact/javax.mail/javax.mail-api
    implementation("javax.mail:javax.mail-api:1.6.2")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-thymeleaf
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.4.3")
    // https://mvnrepository.com/artifact/com.google.cloud/google-cloud-storage
    implementation("com.google.cloud:google-cloud-storage:2.48.2")


}

tasks.withType<Test> {
    useJUnitPlatform()
}
