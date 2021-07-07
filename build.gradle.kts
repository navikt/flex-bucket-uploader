import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.ServiceFileTransformer
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "no.nav.syfo"
version = "1.0.0"

val coroutinesVersion = "1.4.3-native-mt"
val javaxActivationVersion = "1.1.1"
val jacksonVersion = "2.12.2"
val kluentVersion = "1.65"
val ktorVersion = "1.5.2"
val logbackVersion = "1.2.3"
val logstashEncoderVersion = "6.6"
val prometheusVersion = "0.10.0"
val smCommonVersion = "1.7cb158e"
val mockkVersion = "1.11.0"
val nimbusdsVersion = "9.7"
val testContainerKafkaVersion = "1.15.2"
val googleCloudVersion = "1.113.14"
val tikaVersion = "1.25"
val metadataExtractorVersion = "2.15.0"
val junitVersion = "5.7.1"

plugins {
    id("org.jmailen.kotlinter") version "3.4.0"
    kotlin("jvm") version "1.4.32"
    id("com.diffplug.spotless") version "5.14.1"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    jacoco
}

buildscript {
    dependencies {
        classpath("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")
        classpath("org.glassfish.jaxb:jaxb-runtime:3.0.0")
        classpath("com.sun.activation:javax.activation:1.2.0")
    }
}

val githubUser: String by project
val githubPassword: String by project

repositories {
    mavenCentral()
    jcenter()
    maven(url = "https://dl.bintray.com/kotlin/ktor")
    maven(url = "https://packages.confluent.io/maven/")
    maven(url = "https://kotlin.bintray.com/kotlinx")
    maven {
        url = uri("https://maven.pkg.github.com/navikt/syfosm-common")
        credentials {
            username = githubUser
            password = githubPassword
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$coroutinesVersion")
    implementation("io.prometheus:simpleclient_hotspot:$prometheusVersion")
    implementation("io.prometheus:simpleclient_common:$prometheusVersion")

    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-client-auth-basic:$ktorVersion")
    implementation("io.ktor:ktor-client-jackson:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("com.google.cloud:google-cloud-storage:$googleCloudVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashEncoderVersion")

    implementation("com.fasterxml.jackson.module:jackson-module-jaxb-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    implementation("org.apache.tika:tika-core:$tikaVersion")
    implementation("com.drewnoakes:metadata-extractor:$metadataExtractorVersion")

    testImplementation("org.amshove.kluent:kluent:$kluentVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("com.nimbusds:nimbus-jose-jwt:$nimbusdsVersion")
    testImplementation("org.testcontainers:kafka:$testContainerKafkaVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion") {
        exclude(group = "org.eclipse.jetty")
    }

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}

tasks.withType<Jar> {
    manifest.attributes["Main-Class"] = "no.nav.syfo.BootstrapKt"
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("PASSED", "FAILED", "SKIPPED")
        }
    }
    named<KotlinCompile>("compileTestKotlin") {
        kotlinOptions.jvmTarget = "12"
    }
}
