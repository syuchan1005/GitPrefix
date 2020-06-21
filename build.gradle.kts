// TODO: https://github.com/JetBrains/gradle-grammar-kit-plugin/issues/23

plugins {
    id("org.jetbrains.intellij") version "0.4.21"
    id("org.jetbrains.kotlin.jvm") version "1.3.70"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.javassist:javassist:3.23.1-GA")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.61")
}

apply(plugin = "idea")
apply(plugin = "org.jetbrains.intellij")
apply(plugin = "kotlin")
apply(plugin = "java")

val sourceCompatibility = JavaVersion.VERSION_1_8
val targetCompatibility = JavaVersion.VERSION_1_8

group = "com.github.syuchan1005"

val compileJava: JavaCompile by tasks
compileJava.options.apply {
    compilerArgs = listOf("-Xlint:deprecation")
    encoding = "UTF-8"
}

// val intellij: org.jetbrains.intellij.tasks.IntelliJInstrumentCodeTask by tasks
intellij {
    pluginName = "GitPrefix"

    // https://www.jetbrains.com/intellij-repository/releases
    // version '193.5662.53'
    type = "IU"

    setPlugins("git4idea")
}

val patchPluginXml: org.jetbrains.intellij.tasks.PatchPluginXmlTask by tasks
patchPluginXml.apply {
    version(null)
    sinceBuild(null)
    untilBuild(null)
}
