package com.github.syuchan1005.gitprefix.grammar;

import com.github.syuchan1005.gitprefix.filetype.PrefixResourceFileType;
import com.github.syuchan1005.gitprefix.filetype.PrefixResourceLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class PrefixResourceFile extends PsiFileBase {
	protected PrefixResourceFile(@NotNull FileViewProvider viewProvider) {
		super(viewProvider, PrefixResourceLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public FileType getFileType() {
		return PrefixResourceFileType.INSTANCE;
	}

	@Override
	public String toString() {
		return "EmojiResource File";
	}
}
