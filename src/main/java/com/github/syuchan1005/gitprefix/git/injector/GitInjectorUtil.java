package com.github.syuchan1005.gitprefix.git.injector;

import com.github.syuchan1005.gitprefix.filetype.PrefixResourceLanguage;
import com.intellij.util.lang.UrlClassLoader;
import git4idea.actions.GitRepositoryAction;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javax.swing.JPanel;

class GitInjectorUtil {
	static void injectClassPath() throws ReflectiveOperationException, IOException {
		ClassLoader git4ideaClassLoader = GitRepositoryAction.class.getClassLoader();
		JarURLConnection jarConn = (JarURLConnection) GitInjectorUtil.class.getResource("").openConnection();
		URL fileURL = jarConn.getJarFileURL();
		Method addURL = UrlClassLoader.class.getDeclaredMethod("addURL", URL.class);
		addURL.setAccessible(true);
		addURL.invoke(git4ideaClassLoader, fileURL);
		addURL.setAccessible(false);
	}

	static void injectClass(ClassPool classPool, GitInjectorManager.InjectorType type) throws NotFoundException, CannotCompileException, IOException {
		CtClass ctClass = classPool.get(type.getInjectClassName());
		ctClass.defrost();

		String src =
				"public void show() { " +
					"ClassLoader loader = com.intellij.lang.Language.findLanguageByID(\"" + PrefixResourceLanguage.myID + "\").getClass().getClassLoader();" +
					"Class clazz = loader.loadClass(\"" + type.getInjectorClassName() + "\");" +
					"java.lang.reflect.Constructor constructor = clazz.getConstructor(new Class[] {com.intellij.openapi.project.Project.class});" +
					"Object injector = constructor.newInstance(new Object[] {myProject});" +
					"java.lang.reflect.Method beforeShow = injector.getClass().getMethod(\"beforeShow\", new Class[]{Object.class});" +
					"java.lang.reflect.Method afterShow  = injector.getClass().getMethod(\"afterShow\" , new Class[]{Object.class});" +
					"beforeShow.invoke(injector, new Object[] {this});" +
					"super.show();" +
					"afterShow.invoke(injector, new Object[] {this});" +
				"}";
		CtMethod make = CtNewMethod.make(src, ctClass);
		ctClass.addMethod(make);

		ctClass.writeFile();
		ctClass.toClass(GitRepositoryAction.class.getClassLoader(), GitRepositoryAction.class.getProtectionDomain());
	}


	static JPanel getPanel(Object object) throws ReflectiveOperationException {
		Method createCenterPanel = object.getClass().getDeclaredMethod("createCenterPanel");
		createCenterPanel.setAccessible(true);
		JPanel panel = (JPanel) createCenterPanel.invoke(object);
		createCenterPanel.setAccessible(false);
		return panel;
	}
}
