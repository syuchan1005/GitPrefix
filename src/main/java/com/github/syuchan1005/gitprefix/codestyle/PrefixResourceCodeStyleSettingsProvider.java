package com.github.syuchan1005.gitprefix.codestyle;

import com.github.syuchan1005.gitprefix.filetype.PrefixResourceLanguage;
import com.intellij.application.options.CodeStyleAbstractConfigurable;
import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.application.options.TabbedLanguageCodeStylePanel;
import com.intellij.psi.codeStyle.CodeStyleConfigurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrefixResourceCodeStyleSettingsProvider extends CodeStyleSettingsProvider {
	@Nullable
	@Override
	public CustomCodeStyleSettings createCustomSettings(CodeStyleSettings settings) {
		return new PrefixResourceCodeStyle(settings);
	}

	@Nullable
	@Override
	public String getConfigurableDisplayName() {
		return "GitPrefix";
	}

	@NotNull
	@Override
	public CodeStyleConfigurable createConfigurable(@NotNull CodeStyleSettings settings, @NotNull CodeStyleSettings modelSettings) {
		return new CodeStyleAbstractConfigurable(settings, modelSettings, "GitPrefix") {
			@Override
			protected CodeStyleAbstractPanel createPanel(CodeStyleSettings settings) {
				return new TabbedLanguageCodeStylePanel(PrefixResourceLanguage.INSTANCE, getCurrentSettings(), settings) {};
			}

			@Nullable
			@Override
			public String getHelpTopic() {
				return null;
			}
		};
	}
}
