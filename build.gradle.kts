import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.gson.gsonDeserializerOf

// TODO: https://github.com/JetBrains/gradle-grammar-kit-plugin/issues/23

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.4.20"
    id("org.jetbrains.intellij") version "0.6.5"
}

repositories {
    mavenCentral()
    jcenter()
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("com.google.code.gson:gson:2.8.6")
        classpath("com.github.kittinunf.fuel:fuel:2.2.3")
        classpath("com.github.kittinunf.fuel:fuel-gson:2.2.3")
        classpath("org.jsoup:jsoup:1.13.1")
        classpath("net.coobird:thumbnailator:0.4.11")
    }
}

val sourceCompatibility = JavaVersion.VERSION_1_8
val targetCompatibility = JavaVersion.VERSION_1_8

group = "com.github.syuchan1005"

val compileJava: JavaCompile by tasks
compileJava.apply {
    sourceCompatibility = sourceCompatibility.toString()
    targetCompatibility = targetCompatibility.toString()
    options.apply {
        compilerArgs = listOf("-Xlint:deprecation")
        encoding = "UTF-8"
    }
}
val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = targetCompatibility.toString()

// val intellij: org.jetbrains.intellij.tasks.IntelliJInstrumentCodeTask by tasks
intellij {
    pluginName = "GitPrefix"

    // https://www.jetbrains.com/intellij-repository/releases
    type = "IC"

    setPlugins("git4idea")
}

val patchPluginXml: org.jetbrains.intellij.tasks.PatchPluginXmlTask by tasks
patchPluginXml.apply {
    version(null)
    sinceBuild(null)
    untilBuild(null)
}

tasks.register<UpdateEmojiTask>("updateEmoji") {
    group = "gitprefix"
    description = "Update EmojiUtil from submodule"
}
compileKotlin.dependsOn("updateEmoji")

open class UpdateEmojiTask : DefaultTask() {
    @org.gradle.api.tasks.TaskAction
    fun update() {
        val config = Config.get(project)
        if (!config.createClass && !config.convertIcon) {
            return
        }
        // TODO: no output = no update = return logic
        runCommand("git submodule update --recursive")

        val emojiNames = File(config.srcEmojiFolderPath).listFiles()
                ?.map { it.name.replace(".png", "") } ?: return

        if (config.convertIcon) {
            File(config.distEmojiFolderPath).mkdirs()

            emojiNames.forEach {
                logger.info("Processing: ${it}.png")
                net.coobird.thumbnailator.Thumbnails.of("${config.srcEmojiFolderPath}${it}.png")
                        .size(16, 16)
                        .toFile("${config.distEmojiFolderPath}${it}.png")
                net.coobird.thumbnailator.Thumbnails.of("${config.srcEmojiFolderPath}${it}.png")
                        .size(32, 32)
                        .toFile("${config.distEmojiFolderPath}${it}@2x.png")
            }
        }

        if (config.createClass) {
            File("./src/main/java/" +
                    config.classPackageName.replace(".", "/") +
                    "/${config.className}.kt")
                    .writeText(generateClassContent(fetchGists(emojiNames)))

        }
    }

    private fun runCommand(command: String) {
        val process = Runtime.getRuntime().exec(command)
        process.inputStream.copyTo(System.out)
        process.errorStream.copyTo(System.out)
        process.waitFor()
    }

    private fun fetchGists(emojiNames: List<String>): Map<String, String> {
        val config = Config.get(project)
        val gistToken = if (config.gistToken.isEmpty()) {
            System.getenv("GITHUB_TOKEN")
        } else {
            config.gistToken
        }
        val (_, _, createData) = Fuel
                .post("https://api.github.com/gists")
                .jsonBody("{\"files\":{\"test.md\":{\"content\":\"${emojiNames.map { ":$it:" }
                        .joinToString(":", ":", ":")}\"}}}")
                .authentication().bearer(gistToken)
                .responseObject(gsonDeserializerOf(GistResponse.Create::class.java))
        val (createResult, _) = createData

        if (createResult == null) {
            throw IllegalStateException("create failed.")
        }

        var readIndex = 0
        val resultMap = org.jsoup.Jsoup.connect(createResult.html_url).get()
                .select(".markdown-body.entry-content > p").first()
                .childNodes()
                .mapNotNull { node ->
                    when (node) {
                        is org.jsoup.nodes.TextNode -> {
                            if (node.text() != ":") {
                                readIndex++
                            }
                        }
                        is org.jsoup.nodes.Element -> {
                            val pair = emojiNames[readIndex] to node.text()
                            readIndex++
                            return@mapNotNull pair
                        }
                    }
                    return@mapNotNull null
                }.toMap()


        val deleteRes = Fuel
                .delete("https://api.github.com/gists/${createResult.id}")
                .authentication().bearer(gistToken)
                .response()

        if (deleteRes.second.statusCode != 204) {
            logger.error("Gist remove failed.\nURL: ${createResult.html_url}\n ID: ${createResult.id}")
            logger.error(deleteRes.first.toString())
            logger.error(deleteRes.second.toString())
        }

        return resultMap
    }

    private fun generateClassContent(emojiList: Map<String, String>): String {
        val config = Config.get(project)

        return """package ${config.classPackageName}

import com.intellij.openapi.util.IconLoader
import com.intellij.util.castSafelyTo
import javax.swing.Icon

class ${config.className} {
    data class EmojiData(val code: String, val icon: Icon)

    companion object {
        @JvmStatic
        val ${config.classMapName}: Map<String, EmojiData> = mapOf(
${emojiList.map { (k, v) -> "                \"$k\" to EmojiData(\"$v\", IconLoader.getIcon(\"/emojis/$k.png\", ${config.className}::class.java))" }
                .joinToString(",\n")}
        )
    }
}
"""
    }

    sealed class GistResponse {
        data class Create(val id: String, val html_url: String) : GistResponse()
    }

    data class Config(
            val createClass: Boolean,
            val convertIcon: Boolean,
            val srcEmojiFolderPath: String,
            val distEmojiFolderPath: String,
            val classPackageName: String,
            val className: String,
            val classMapName: String,
            val gistToken: String
    ) {
        companion object {
            private val gson = com.google.gson.Gson()

            private var config: Config? = null

            fun get(p: Project): Config {
                if (config == null) {
                    var jsonFile = p.file("EmojiUpdate.json")
                    if (!jsonFile.exists()) jsonFile = p.file("./EmojiUpdate.template.json")
                    config = gson.fromJson(jsonFile.reader(), Config::class.java)
                }

                return config!!
            }
        }
    }
}
