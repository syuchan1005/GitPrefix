package com.github.syuchan1005.gitprefix;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "GitPrefixData", storages = { @Storage(StoragePathMacros.WORKSPACE_FILE) })
public class GitPrefixData implements PersistentStateComponent<GitPrefixData> {
	private String isPathType = "DEFAULT";
	private String gitPrefixPath = "";

	@Nullable
	@Override
	public GitPrefixData getState() {
		return this;
	}

	@Override
	public void loadState(@NotNull GitPrefixData state) {
		XmlSerializerUtil.copyBean(state, this);
	}

	@Nullable
	public static GitPrefixData getInstance(Project project) {
		return ServiceManager.getService(project, GitPrefixData.class);
	}

	public String getIsPathType() {
		return isPathType;
	}

	public String getGitPrefixPath() {
		return gitPrefixPath;
	}

	public void setIsPathType(String isPathType) {
		this.isPathType = isPathType;
	}

	public void setGitPrefixPath(String gitPrefixPath) {
		this.gitPrefixPath = gitPrefixPath;
	}
}
