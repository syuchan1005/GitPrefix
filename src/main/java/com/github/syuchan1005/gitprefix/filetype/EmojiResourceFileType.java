package com.github.syuchan1005.gitprefix.filetype;

import com.intellij.openapi.fileTypes.LanguageFileType;
import icons.GitPrefixIcons;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmojiResourceFileType extends LanguageFileType {
	public static final EmojiResourceFileType INSTANCE = new EmojiResourceFileType();

	public static final String DEFAULT_EXTENSION = "emojirc";

	private EmojiResourceFileType() {
		super(EmojiResourceLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getName() {
		return "EmojiPrefix Resource";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "EmojiPrefix Resource";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return DEFAULT_EXTENSION;
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return GitPrefixIcons.FILE_ICON_OLD;
	}
}

