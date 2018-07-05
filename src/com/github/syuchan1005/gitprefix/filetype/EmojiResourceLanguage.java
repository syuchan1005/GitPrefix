package com.github.syuchan1005.gitprefix.filetype;

import com.intellij.lang.Language;

public class EmojiResourceLanguage extends Language {
	public static EmojiResourceLanguage INSTANCE = new EmojiResourceLanguage();

	private EmojiResourceLanguage() {
		super("EmojiPrefix Resource (GitPrefix Plugin)");
	}
}
