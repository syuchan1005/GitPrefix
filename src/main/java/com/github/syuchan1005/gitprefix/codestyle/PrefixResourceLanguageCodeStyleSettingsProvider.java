package com.github.syuchan1005.gitprefix.codestyle;

import com.github.syuchan1005.gitprefix.filetype.PrefixResourceLanguage;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrefixResourceLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {
	@NotNull
	@Override
	public Language getLanguage() {
		return PrefixResourceLanguage.INSTANCE;
	}

	@Nullable
	@Override
	public String getCodeSample(@NotNull SettingsType settingsType) {
		return "commit {\n:tada: test\n}";
	}
}
