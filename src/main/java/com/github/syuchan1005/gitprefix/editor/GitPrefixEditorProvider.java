package com.github.syuchan1005.gitprefix.editor;

import com.github.syuchan1005.gitprefix.filetype.PrefixResourceFileType;
import com.github.syuchan1005.gitprefix.grammar.PrefixResourceElementFactory;
import com.github.syuchan1005.gitprefix.grammar.PrefixResourceFile;
import com.github.syuchan1005.gitprefix.ui.EmojiListEditor;
import com.github.syuchan1005.gitprefix.ui.TextAndPreviewEditor;
import com.github.syuchan1005.gitprefix.util.PrefixResourceFileUtil;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

public class GitPrefixEditorProvider implements FileEditorProvider {
	@Override
	public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
		return file.getFileType() instanceof PrefixResourceFileType;
	}

	@NotNull
	@Override
	public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
		PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
		if (!(psiFile instanceof PrefixResourceFile))
			return TextEditorProvider.getInstance().createEditor(project, file);
		TextEditor mainEditor = (TextEditor) TextEditorProvider.getInstance().createEditor(project, file);

		PrefixResourceFile structuredFile = PrefixResourceFileUtil.createStructuredFile((PrefixResourceFile) psiFile, true);
		TextEditor structuredPreviewEditor = (TextEditor) TextEditorProvider.getInstance().createEditor(
				project, structuredFile.getViewProvider().getVirtualFile());


		mainEditor.getEditor().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void documentChanged(@NotNull DocumentEvent event) {
				PrefixResourceFile file = PrefixResourceElementFactory.createFile(project, event.getDocument().getText());
				file = PrefixResourceFileUtil.createStructuredFile(file, true);
				structuredPreviewEditor.getEditor().getDocument().setText(file.getText());
			}
		});

		return new TextAndPreviewEditor(mainEditor, new EmojiListEditor(), structuredPreviewEditor);
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
