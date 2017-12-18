package com.github.syuchan1005.emojiprefix.filetype;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmojiResourceFileType extends LanguageFileType {
	public static final EmojiResourceFileType INSTANCE = new EmojiResourceFileType();
	private static final String DEFAULT_EXTENSION = "emojirc";
	private static final Icon FILE_ICON = IconLoader.getIcon("/icons/fileIcon.png");

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
		return FILE_ICON;
	}
}
