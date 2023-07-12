Проект показывает как использовать в основном коде старую версию котлина, а в тестах более новую.

### Изменение версии
```kotlin
tasks.withType<KotlinCompile> {
    kotlinOptions {
        languageVersion = when {
            name.contains("test", ignoreCase = true) -> "1.7"
            else -> "1.5"
        }
        jvmTarget = "1.8"
    }
}
```
Для каждой таски можно настроить свою версию котлина. Тут для тестов устанавливает 1.7, а для основного 1.5.

### Изменение зависимости
```kotlin
configurations.configureEach {
    withDependencies {
        filterIsInstance<ExternalDependency>()
            .filter { it.group == "org.jetbrains.kotlin" }
            .forEach {
                val version = when {
                    name.contains("test", ignoreCase = true) || name == "kotlinCompilerClasspath" -> "1.7.21"
                    else -> "1.5.32"
                }
                it.version { require(version) }
            }
    }
}
```
При подключении котлин плагина он уже сам добавляет в зависимости котлин той же версии, что и плагин. 
Если просто в `dependencies` прописать зависимость от котлина, то возьмется более высокая версия, поэтому так не получиться.
Через `require` мы говорим градлу, что хотим использовать именно такую версию, даже если где-то указана более высокая.

## Проверка

### Android

- Выполните сборку модуля `android-library` командой `:android-library:assemble`. 
  Откройте файл `build/tmp/kotlin-classes/demoGoogleDebug/LibKt.class` и в меню выбрать `Tools | Kotlin | Decompile to Java`.
  В аннотации `@Metadata` в параметре `mv` идет совместимая версия котлина. В данном случае `1.5.1`
- Запустите тест [Test.kt](android-library%2Fsrc%2FandroidTest%2Fkotlin%2FTest.kt). Он должен быть успешным.
- Сгенерируйте [pom.xml](android-library%2Fbuild%2Fpublications%2FfullGoogleRelease%2Fpom-default.xml) командой `:android-library:generatePomFileForFullGoogleReleasePublication`.
  В нем должна быть зависимость от котлин 1.5.32
- Выполните сборку модуля `android-library-test` командой `:android-library-test:assemble`.
  В нем используется котлин 1.5.32 и таким образом проверяем, что такие проекты будут успешно работать с нашей либой.

### Kotlin

- Выполните сборку модуля `kotlin-library` командой `:kotlin-library:assemble`.
  Откройте файл `build/classes/kotlin/main/LibKt.class` и в меню выбрать `Tools | Kotlin | Decompile to Java`.
  В аннотации `@Metadata` в параметре `mv` идет совместимая версия котлина. В данном случае `1.5.1`
- Запустите тест [Test.kt](kotlin-library%2Fsrc%2Ftest%2Fkotlin%2FTest.kt). Он должен быть успешным.
- Сгенерируйте [pom.xml](kotlin-library%2Fbuild%2Fpublications%2Flib%2Fpom-default.xml) командой `:kotlin-library:generateMetadataFileForLibPublication`.
  В нем должна быть зависимость от котлин 1.5.32
- Выполните сборку модуля `kotlin-library-test` командой `:kotlin-library-test:assemble`.
  В нем используется котлин 1.5.32 и таким образом проверяем, что такие проекты будут успешно работать с нашей либой.