// Файл верхнего уровня build.gradle.kts
// Здесь только общие настройки проекта, без android-application плагина

plugins {
    // Плагины верхнего уровня для всего проекта
    // Не включаем здесь android-application или kotlin-android,
    // так как они должны быть применены только в модуле app
    base // Базовый плагин для стандартных задач, таких как clean
}

// Настройки для всех подпроектов
subprojects {
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
