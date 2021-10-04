package com.github.syuchan1005.gitprefix;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "GitPrefixData", storages = {@Storage(StoragePathMacros.WORKSPACE_FILE)})
public class GitPrefixData implements PersistentStateComponent<GitPrefixData> {
	private PathType pathType = PathType.DEFAULT;
	private String gitPrefixPath = "";

	public enum PathType {
		DEFAULT,
		CUSTOM;
	}

	@Nullable
	public static GitPrefixData getInstance(Project project) {
		return project.getComponent(GitPrefixData.class);
	}

	@Nullable
	@Override
	public GitPrefixData getState() {
		return this;
	}

	@Override
	public void loadState(@NotNull GitPrefixData state) {
		XmlSerializerUtil.copyBean(state, this);
	}

	public PathType getPathType() {
		return pathType;
	}

	public void setPathType(PathType pathType) {
		this.pathType = pathType;
	}

	public String getGitPrefixPath() {
		return gitPrefixPath;
	}

	public void setGitPrefixPath(String gitPrefixPath) {
		this.gitPrefixPath = gitPrefixPath;
	}
}
