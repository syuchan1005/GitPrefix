package com.github.syuchan1005.emojiprefix.filetype;

import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;

public class EmojiResourceFileTypeFactory extends FileTypeFactory {
	@Override
	public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
		fileTypeConsumer.consume(EmojiResourceFileType.INSTANCE, new FileNameMatcher() {
			@Override
			public boolean accept(@NotNull String s) {
				return s.equals(".emojirc");
			}

			@NotNull
			@Override
			public String getPresentableString() {
				return ".emojirc";
			}
		});
	}
}
