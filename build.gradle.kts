fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

plugins {
    id("org.jetbrains.intellij") version "1.16.0"
    id("java")
}

group = properties("pluginGroup").get()
version = properties("pluginVersion").get()

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter-engine:5.10.3")
    implementation("com.github.javaparser:javaparser-symbol-solver-core:3.25.6")
    implementation("net.sourceforge.plantuml:plantuml:1.2023.12") // 示例版本
//    implementation("org.apache.xmlgraphics:batik-all:1.14")
//    implementation("xerces:xercesImpl:2.12.1")  // 如果需要 XML 解析支持
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2023.2.3"
    type = properties("platformType")
    plugins = properties("platformPlugins").map { it.split(',').map(String::trim).filter(String::isNotEmpty) }
}

tasks.test {
    useJUnitPlatform()
}
tasks {
    patchPluginXml{
        version = properties("pluginVersion")
    }
    patchPluginXml {
        version = properties("pluginVersion")
        sinceBuild = properties("pluginSinceBuild")
        untilBuild = properties("pluginUntilBuild")
    }
    runIde {
        autoReloadPlugins.set(true)
    }
    // 解决插件中显示中文乱码的问题
    withType(JavaCompile::class.java) {
        options.encoding = "UTF-8"
    }
}