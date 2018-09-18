package com.github.syuchan1005.gitprefix.filetype;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.github.syuchan1005.gitprefix.icons.GitPrefixIcons;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrefixResourceFileType extends LanguageFileType {
	public static final PrefixResourceFileType INSTANCE = new PrefixResourceFileType();

	public static final String DEFAULT_EXTENSION = "gitprefix";

	private PrefixResourceFileType() {
		super(PrefixResourceLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getName() {
		return "GitPrefix Resource";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "GitPrefix Resource";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return DEFAULT_EXTENSION;
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return GitPrefixIcons.FILE_ICON;
	}
}
