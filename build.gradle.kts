plugins {
    id("java")
    id("war")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")

    // JSF API
    implementation("jakarta.faces:jakarta.faces-api:3.0.0")
    // JSF Implementation (Mojarra)
    implementation("org.glassfish:jakarta.faces:3.0.0")

    // PrimeFaces библиотека
    implementation("org.primefaces:primefaces:12.0.0:jakarta")
    // ICEFaces ядро
//    implementation("org.icefaces:icefaces:4.3.0")

    implementation("com.sun.xml.messaging.saaj:saaj-impl:3.0.4")

    // JPA API
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    // Hibernate ORM
    implementation("org.hibernate:hibernate-core:6.2.10.Final")

    // Драйвер для PostgreSQL
    implementation("org.postgresql:postgresql:42.6.0")

    providedRuntime("jakarta.servlet:jakarta.servlet-api:5.0.0")
//    implementation("jakarta.platform:jakarta.jakartaee-web-api:10.0.0")
    implementation("jakarta.enterprise:jakarta.enterprise.cdi-api:3.0.0")
}


val remoteHost = "helios.cs.ifmo.ru"
val remoteUser = "s408536"
val remoteDir = "~/web-3/"
val warFile = "build/libs/web-3-1.0-SNAPSHOT.war"

tasks.register<Exec>("copyWarToServer") {
    dependsOn(tasks.build)

    doLast {
        println("Copying WAR to $remoteUser@$remoteHost:$remoteDir")
    }

    commandLine("scp", "-P", "2222", warFile, "$remoteUser@$remoteHost:$remoteDir")

    doLast {
        println("WAR file copied successfully.")
    }
}



tasks.test {
    useJUnitPlatform()
}