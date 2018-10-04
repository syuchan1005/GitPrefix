package com.github.syuchan1005.gitprefix.codestyle;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import org.jetbrains.annotations.NotNull;

public class PrefixResourceCodeStyle extends CustomCodeStyleSettings {
	public PrefixResourceCodeStyle(CodeStyleSettings container) {
		super("PrefixResourceCodeStyle", container);
	}
}
