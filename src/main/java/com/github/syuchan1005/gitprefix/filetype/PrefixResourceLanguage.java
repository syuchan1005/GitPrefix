package com.github.syuchan1005.gitprefix.filetype;

import com.intellij.lang.Language;

public class PrefixResourceLanguage extends Language {
	public static PrefixResourceLanguage INSTANCE = new PrefixResourceLanguage();

	private PrefixResourceLanguage() {
		super("GitPrefix Resource");
	}
}
