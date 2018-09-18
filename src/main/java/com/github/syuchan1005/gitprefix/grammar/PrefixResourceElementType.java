package com.github.syuchan1005.gitprefix.grammar;

import com.github.syuchan1005.gitprefix.filetype.PrefixResourceLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class PrefixResourceElementType extends IElementType {
	public PrefixResourceElementType(@NotNull String debugName) {
		super(debugName, PrefixResourceLanguage.INSTANCE);
	}

	@Override
	public String toString() {
		return "PrefixResourceElementType." + super.toString();
	}
}
