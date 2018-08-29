package com.github.syuchan1005.gitprefix.git.injector;

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

public class GitInjectorUtil {
	public static void injectClassPath() throws ReflectiveOperationException, IOException {
		ClassLoader git4ideaClassLoader = GitRepositoryAction.class.getClassLoader();
		JarURLConnection jarConn = (JarURLConnection) GitInjectorUtil.class.getResource("").openConnection();
		URL fileURL = jarConn.getJarFileURL();
		Method addURL = UrlClassLoader.class.getDeclaredMethod("addURL", URL.class);
		addURL.setAccessible(true);
		addURL.invoke(git4ideaClassLoader, fileURL);
		addURL.setAccessible(false);
	}

	public static void injectClass(ClassPool classPool, GitInjectorManager.InjectorType type) throws NotFoundException, CannotCompileException, IOException {
		CtClass ctClass = classPool.get(type.getInjectClassName());
		ctClass.defrost();

		String src = "public void show() { " +
				AbstractGitDialogInjector.class.getCanonicalName() + " injector = " + type.toString() + ".getOrCreateInjector(myProject);" +
				"injector.beforeShow(this);" +
				"super.show();" +
				"injector.afterShow(this);" +
				"}";
		CtMethod make = CtNewMethod.make(src, ctClass);
		ctClass.addMethod(make);

		ctClass.writeFile();
		ctClass.toClass(GitRepositoryAction.class.getClassLoader(), GitRepositoryAction.class.getProtectionDomain());
	}


	public static JPanel getPanel(Object object) throws ReflectiveOperationException {
		Method createCenterPanel = object.getClass().getDeclaredMethod("createCenterPanel");
		createCenterPanel.setAccessible(true);
		JPanel panel = (JPanel) createCenterPanel.invoke(object);
		createCenterPanel.setAccessible(false);
		return panel;
	}
}
