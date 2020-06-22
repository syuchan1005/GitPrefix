package com.github.syuchan1005.gitprefix.git.injector

import com.github.syuchan1005.gitprefix.filetype.PrefixResourceLanguage
import com.github.syuchan1005.gitprefix.git.injector.GitInjectorManager.InjectorType
import git4idea.actions.GitRepositoryAction
import javassist.CannotCompileException
import javassist.ClassPool
import javassist.CtNewMethod
import javassist.NotFoundException
import java.io.IOException
import javax.swing.JPanel

internal object GitInjectorUtil {
    @JvmStatic
    @Throws(NotFoundException::class, CannotCompileException::class, IOException::class)
    fun injectClass(classPool: ClassPool, type: InjectorType) {
        val ctClass = classPool[type.injectClassName]
        ctClass.defrost()
        // language=JAVA
        val make = CtNewMethod.make("""
            public void show() {
                ClassLoader loader = com.intellij.lang.Language.findLanguageByID("${PrefixResourceLanguage.myID}").getClass().getClassLoader();
                Class clazz = loader.loadClass("${type.injectorClassName}");
                java.lang.reflect.Constructor constructor = clazz.getConstructor(new Class[] {com.intellij.openapi.project.Project.class});
                Object injector = constructor.newInstance(new Object[] {${type.projectObjectName}});
                java.lang.reflect.Method beforeShow = injector.getClass().getMethod("beforeShow", new Class[]{Object.class});
                java.lang.reflect.Method afterShow  = injector.getClass().getMethod("afterShow" , new Class[]{Object.class});
                beforeShow.invoke(injector, new Object[] {this});
                super.show();
                afterShow.invoke(injector, new Object[] {this});
            }
        """.trimIndent(), ctClass)
        ctClass.addMethod(make)
        ctClass.writeFile()
        ctClass.toClass(GitRepositoryAction::class.java.classLoader, GitRepositoryAction::class.java.protectionDomain)
    }

    @JvmStatic
    @Throws(ReflectiveOperationException::class)
    fun getPanel(`object`: Any): JPanel {
        val createCenterPanel = `object`.javaClass.getDeclaredMethod("createCenterPanel")
        createCenterPanel.isAccessible = true
        val panel = createCenterPanel.invoke(`object`) as JPanel
        createCenterPanel.isAccessible = false
        return panel
    }
}