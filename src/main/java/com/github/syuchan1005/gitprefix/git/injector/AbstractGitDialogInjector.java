package com.github.syuchan1005.gitprefix.git.injector;

import com.intellij.openapi.project.Project;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractGitDialogInjector {
	protected GitInjectorManager.InjectorType myType;
	protected Project myProject;
	protected JPanel myCenterPanel;

	public AbstractGitDialogInjector(@NotNull GitInjectorManager.InjectorType type, @NotNull Project project) {
		this.myType = type;
		this.myProject = project;
	}

	public void beforeShow(Object dialog) throws Exception {
		this.myCenterPanel = GitInjectorUtil.getPanel(dialog);
	}

	public void afterShow(Object dialog) throws Exception {
		this.myType.removeInjector(myProject);
	}
}
