package com.github.syuchan1005.emojiprefix.psi;

import com.intellij.lexer.FlexAdapter;

public class EmojiResourceLexerAdapter extends FlexAdapter {
	public EmojiResourceLexerAdapter() {
		super(new EmojiResourceLexer(null));
	}
}
