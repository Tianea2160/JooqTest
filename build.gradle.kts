import org.jooq.meta.jaxb.Property

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("nu.studer.jooq") version "9.0"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"
val jooqVersion = "3.19.21"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.boot:spring-boot-starter-jooq")
    jooqGenerator("org.jooq:jooq-meta-extensions:$jooqVersion")
    implementation("org.jooq:jooq-codegen:$jooqVersion")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jooq {
    version.set(jooqVersion)
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)  // the default (can be omitted)
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(true)
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.INFO
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.extensions.ddl.DDLDatabase"

                        properties.addAll(
                            listOf(
                                Property().let { it ->
                                    it.key = "scripts"
                                    it.value = "src/main/resources/db/init.sql"
                                    it
                                },
                                Property().let { it ->
                                    it.key = "defaultNameCase"
                                    it.value = "lower"
                                    it
                                },
                                Property().let { it ->
                                    it.key = "unqualifiedSchema"
                                    it.value = "none"
                                    it
                                },
                                Property().let { it ->
                                    it.key = "sort"
                                    it.value = "semantic"
                                    it
                                }
                            )
                        )

                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "nu.studer.sample"
                        directory = "build/generated-src/jooq/main"  // default (can be omitted)
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}