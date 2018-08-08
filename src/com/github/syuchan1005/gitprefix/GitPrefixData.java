package com.github.syuchan1005.gitprefix;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GitPrefixData implements PersistentStateComponent<GitPrefixData> {
	public boolean isShowFirstNotification = false;

	@Nullable
	@Override
	public GitPrefixData getState() {
		return this;
	}

	@Override
	public void loadState(@NotNull GitPrefixData state) {
		XmlSerializerUtil.copyBean(state, this);
	}
}
