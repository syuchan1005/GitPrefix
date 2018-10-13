package com.github.syuchan1005.gitprefix.setting;

import com.github.syuchan1005.gitprefix.GitPrefixData;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import javax.swing.JComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GitPrefixConfigurable implements SearchableConfigurable {
	private Project myProject;
	private GitPrefixConfigurableGUI gui = null;
	private GitPrefixData prefixData;

	public GitPrefixConfigurable(Project project) {
		this.myProject = project;
		this.prefixData = GitPrefixData.getInstance(myProject);
	}

	@NotNull
	@Override
	public String getId() {
		return "perference.gitprefix.GitPrefixConfigurable";
	}

	@Override
	public String getDisplayName() {
		return "GitPrefix";
	}

	@Nullable
	@Override
	public JComponent createComponent() {
		gui = new GitPrefixConfigurableGUI(this.myProject, this.prefixData.getPathType(), this.prefixData.getGitPrefixPath());
		return gui.getRootPane();
	}

	@Override
	public void disposeUIResources() {
		gui = null;
	}

	@Override
	public boolean isModified() {
		return gui != null && gui.isModified();
	}

	@Override
	public void apply() {
		this.prefixData.setPathType(gui.getDefaultRadioButton().isSelected() ? GitPrefixData.PathType.DEFAULT : GitPrefixData.PathType.CUSTOM);
		this.prefixData.setGitPrefixPath(gui.getPathField().getText());
	}

	@Nullable
	@Override
	public String getHelpTopic() {
		return null;
	}

	@Nullable
	@Override
	public Runnable enableSearch(String option) {
		return null;
	}

	@Override
	public void reset() {
	}
}
