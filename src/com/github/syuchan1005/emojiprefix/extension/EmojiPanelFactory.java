package com.github.syuchan1005.emojiprefix.extension;

import com.intellij.openapi.vcs.ui.CommitMessage;
import org.jetbrains.annotations.NotNull;

public interface EmojiPanelFactory {
	void createPanel(@NotNull CommitMessage commitMessage);

	ReturnResult beforeCheckin();

	enum ReturnResult {
		OK,
		CANCEL,
	}
}
