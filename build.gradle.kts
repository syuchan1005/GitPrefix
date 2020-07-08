import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.gson.gsonDeserializerOf

// TODO: https://github.com/JetBrains/gradle-grammar-kit-plugin/issues/23

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

apply {
    plugin("idea")
    plugin("org.jetbrains.intellij")
    plugin("kotlin")
    plugin("java")
}

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
    type = "IU"
    // type = "PY"; version = "2020.1.2"

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
val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.dependsOn("updateEmoji")

open class UpdateEmojiTask : DefaultTask() {
    @org.gradle.api.tasks.TaskAction
    fun update() {
        val config = Config.get()
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
        val config = Config.get()
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
        val config = Config.get()

        return """package ${config.classPackageName}

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

class ${config.className} {
    data class EmojiData(val code: String, val icon: Icon)

    companion object {
        @JvmStatic
        val ${config.classMapName}: Map<String, EmojiData> = mapOf(
${emojiList.map { (k, v) -> "                \"$k\" to EmojiData(\"$v\", IconLoader.getIcon(\"/emojis/$k.png\"))" }
                .joinToString(",\n")}
        )
    }
}
""".trimIndent()
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
            private var config: Config? = null

            fun get(): Config {
                if (config == null) {
                    var file = File("./EmojiUpdate.json")
                    if (!file.exists()) file = File("./EmojiUpdate.template.json")
                    config = com.google.gson.Gson()
                            .fromJson(file.reader(), Config::class.java)
                }

                return config!!
            }
        }
    }
}
