package com.github.syuchan1005.gitprefix.filetype;

import com.intellij.lang.Language;

public class PrefixResourceLanguage extends Language {
	public static final String myID = "GitPrefix Resource";

	public static Language INSTANCE;
	static {
		INSTANCE = Language.findLanguageByID(myID);
		if (INSTANCE == null) INSTANCE = new PrefixResourceLanguage();
	}

	private PrefixResourceLanguage() {
		super(myID);
	}
}
