📘 DDL 기반 JOOQ 사용 실습

이 프로젝트는 src/main/resources/db/init.sql에 정의된 DDL SQL 파일을 기반으로
JOOQ DSL 코드(Java/Kotlin)를 자동 생성하고, 이를 통해 타입 안전한 SQL 쿼리를 작성할 수 있도록 구성되어 있습니다.

⸻

🔧 핵심 설정 (build.gradle.kts 내 jooq {} 블록)
```kotlin
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
```

📁 구조 개요
```text
src/
  main/
    resources/
      db/
        init.sql         ← ✅ DDL 정의
  generated-src/
    jooq/
      main/
        nu/
          studer/
            sample/
              Tables.kt, Records.kt, ...
```