package com.github.syuchan1005.gitprefix.git.injector

import com.github.syuchan1005.gitprefix.git.injector.GitInjectorUtil.injectClass
import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.application.ApplicationInfo
import git4idea.actions.GitRepositoryAction
import javassist.ClassClassPath
import javassist.ClassPool
import javassist.LoaderClassPath

class GitInjectorManager : AppLifecycleListener {
    override fun appFrameCreated(commandLineArgs: List<String>) {
        try {
            val classPool = ClassPool.getDefault()
            classPool.appendClassPath(ClassClassPath(this.javaClass))
            classPool.appendClassPath(LoaderClassPath(GitRepositoryAction::class.java.classLoader))
            for (type in InjectorType.values()) {
                injectClass(classPool, type)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Reference: https://github.com/JetBrains/intellij-community/tree/master/plugins/git4idea
    enum class InjectorType(val clazz: Class<out AbstractGitDialogInjector?>, val injectClassName: String, val injectorClassName: String) {
        TAG(GitTagDialogInjector::class.java, "git4idea.ui.GitTagDialog",
                "com.github.syuchan1005.gitprefix.git.injector.GitTagDialogInjector"),
        MERGE(GitMergeDialogInjector::class.java, "git4idea.merge.GitMergeDialog",
                "com.github.syuchan1005.gitprefix.git.injector.GitMergeDialogInjector");

        val projectObjectName: String
            get() {
                val versionNum = ApplicationInfo.getInstance().build.components[0]
                return if (versionNum >= 202 && this != TAG) {
                    "project"
                } else "myProject"
            }

        override fun toString(): String {
            return InjectorType::class.java.canonicalName + "." + super.toString()
        }

    }
}