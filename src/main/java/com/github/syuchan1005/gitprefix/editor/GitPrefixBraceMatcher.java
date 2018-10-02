package com.github.syuchan1005.gitprefix.editor;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes.*;

public class GitPrefixBraceMatcher implements PairedBraceMatcher {
	private static BracePair[] PAIRS = new BracePair[] {
		new BracePair(LEFT_BRACE, RIGHT_BRACE, true),
	};

	@NotNull
	@Override
	public BracePair[] getPairs() {
		return PAIRS;
	}

	@Override
	public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
		return true;
	}

	@Override
	public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
		return openingBraceOffset;
	}
}
