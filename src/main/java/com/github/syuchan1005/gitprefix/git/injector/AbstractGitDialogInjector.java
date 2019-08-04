package com.github.syuchan1005.gitprefix.git.injector;

import com.intellij.openapi.project.Project;
import javax.swing.JPanel;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class AbstractGitDialogInjector {
	Project myProject;
	JPanel myCenterPanel;

	AbstractGitDialogInjector(@NotNull Project project) {
		this.myProject = project;
	}

	public void beforeShow(Object dialog) throws Exception {
		this.myCenterPanel = GitInjectorUtil.getPanel(dialog);
	}

	public void afterShow(Object dialog) throws Exception { }

	public Object getGridConstraints(int index) throws ReflectiveOperationException {
		Field myConstraints = this.myCenterPanel.getLayout().getClass().getSuperclass().getDeclaredField("myConstraints");
		myConstraints.setAccessible(true);
		Object g = ((Object[]) myConstraints.get(this.myCenterPanel.getLayout()))[index];
		myConstraints.setAccessible(false);
		return g;
		// ((GridLayoutManager) root.getLayout()).getConstraintsForComponent(comp)
	}
}
