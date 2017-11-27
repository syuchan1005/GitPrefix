package com.github.syuchan1005.emojiprefix.psi;

import com.github.syuchan1005.emojiprefix.filetype.EmojiResourceLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class EmojiResourceElementType extends IElementType {
	public EmojiResourceElementType(@NotNull String debugName) {
		super(debugName, EmojiResourceLanguage.INSTANCE);
	}

	@Override
	public String toString() {
		return "EmojiResourceElementType." + super.toString();
	}
}
