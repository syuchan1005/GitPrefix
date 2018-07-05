package com.github.syuchan1005.gitprefix.extension;

import com.intellij.openapi.vcs.ui.CommitMessage;
import org.jetbrains.annotations.NotNull;

public interface PrefixPanelFactory {
	void createPanel(@NotNull CommitMessage commitMessage);

	ReturnResult beforeCheckin();

	enum ReturnResult {
		OK,
		CANCEL,
	}
}
