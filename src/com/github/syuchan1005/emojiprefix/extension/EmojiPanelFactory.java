package com.github.syuchan1005.emojiprefix.extension;

import com.intellij.openapi.extensions.AbstractExtensionPointBean;
import com.intellij.openapi.vcs.ui.CommitMessage;
import com.intellij.util.xmlb.annotations.Attribute;
import org.jetbrains.annotations.NotNull;

public class EmojiPanelFactory extends AbstractExtensionPointBean {
	@Attribute("implementationClass")
	public String implementationClass;

	public EmojiPanelFactory() {
	}

	public void createPanel(@NotNull CommitMessage commitMessage) {
	}

	public ReturnResult beforeCheckin() {
		return ReturnResult.OK;
	}

	public enum ReturnResult {
		OK,
		CANCEL,
	}
}
