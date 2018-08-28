package com.github.syuchan1005.gitprefix.editor;

import com.github.syuchan1005.gitprefix.filetype.PrefixResourceFileType;
import com.github.syuchan1005.gitprefix.ui.EmojiListEditor;
import com.github.syuchan1005.gitprefix.ui.TextAndPreviewEditor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class GitPrefixEditorProvider implements FileEditorProvider {
	@Override
	public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
		return file.getFileType() instanceof PrefixResourceFileType;
	}

	@NotNull
	@Override
	public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
		return new TextAndPreviewEditor(
				(TextEditor) TextEditorProvider.getInstance().createEditor(project, file), new EmojiListEditor());
	}

	@NotNull
	@Override
	public String getEditorTypeId() {
		return "gitprefix.fileEditor.withEmojiList";
	}

	@NotNull
	@Override
	public FileEditorPolicy getPolicy() {
		return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
	}
}
