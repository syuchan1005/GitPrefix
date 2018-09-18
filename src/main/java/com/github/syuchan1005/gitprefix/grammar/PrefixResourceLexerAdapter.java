package com.github.syuchan1005.gitprefix.grammar;

import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceLexer;
import com.intellij.lexer.FlexAdapter;

public class PrefixResourceLexerAdapter extends FlexAdapter {
	public PrefixResourceLexerAdapter() {
		super(new PrefixResourceLexer(null));
	}
}
