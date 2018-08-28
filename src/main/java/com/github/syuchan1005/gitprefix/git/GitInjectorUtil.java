package com.github.syuchan1005.gitprefix.git;

import com.intellij.openapi.project.Project;
import com.intellij.util.lang.UrlClassLoader;
import git4idea.actions.GitRepositoryAction;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import javax.swing.JPanel;

public class GitInjectorUtil {
	private static boolean isInjectedClassPath = false;

	public static void injectClassPath() throws ReflectiveOperationException, IOException {
		if (isInjectedClassPath) return;
		isInjectedClassPath = true;
		ClassLoader git4ideaClassLoader = GitRepositoryAction.class.getClassLoader();
		JarURLConnection jarConn = (JarURLConnection) GitInjectorUtil.class.getResource("").openConnection();
		URL fileURL = jarConn.getJarFileURL();
		Method addURL = UrlClassLoader.class.getDeclaredMethod("addURL", URL.class);
		addURL.setAccessible(true);
		addURL.invoke(git4ideaClassLoader, fileURL);
		addURL.setAccessible(false);
	}

	public static JPanel getPanel(Object object) throws ReflectiveOperationException {
		Method createCenterPanel = object.getClass().getDeclaredMethod("createCenterPanel");
		createCenterPanel.setAccessible(true);
		JPanel panel = (JPanel) createCenterPanel.invoke(object);
		createCenterPanel.setAccessible(false);
		return panel;
	}

	public static Project getProject(Object object) throws ReflectiveOperationException {
		Field myProject = object.getClass().getDeclaredField("myProject");
		myProject.setAccessible(true);
		Project project = (Project) myProject.get(object);
		myProject.setAccessible(false);
		return project;
	}
}
