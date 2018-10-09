package com.github.syuchan1005.gitprefix.grammar;

import com.github.syuchan1005.gitprefix.filetype.PrefixResourceLanguage;
import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class PrefixResourceTokenType extends IElementType {
	public PrefixResourceTokenType(@NotNull String debugName) {
		super(debugName, PrefixResourceLanguage.INSTANCE);
	}

	@Override
	public String toString() {
		return "PrefixResourceTokenType." + super.toString();
	}
}
