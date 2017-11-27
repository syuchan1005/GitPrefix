package com.github.syuchan1005.emojiprefix.psi;

import com.github.syuchan1005.emojiprefix.filetype.EmojiResourceFileType;
import com.github.syuchan1005.emojiprefix.filetype.EmojiResourceLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class EmojiResourceFile extends PsiFileBase {
	protected EmojiResourceFile(@NotNull FileViewProvider viewProvider) {
		super(viewProvider, EmojiResourceLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public FileType getFileType() {
		return EmojiResourceFileType.INSTANCE;
	}

	@Override
	public String toString() {
		return "EmojiResource File";
	}
}
