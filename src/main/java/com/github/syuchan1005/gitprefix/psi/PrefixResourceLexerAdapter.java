package com.github.syuchan1005.gitprefix.psi;

import com.intellij.lexer.FlexAdapter;

public class PrefixResourceLexerAdapter extends FlexAdapter {
	public PrefixResourceLexerAdapter() {
		super(new PrefixResourceLexer(null));
	}
}
