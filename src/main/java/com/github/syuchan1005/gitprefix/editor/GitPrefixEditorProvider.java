package com.github.syuchan1005.gitprefix.editor;

import com.github.syuchan1005.gitprefix.filetype.PrefixResourceFileType;
import com.github.syuchan1005.gitprefix.grammar.PrefixResourceElementFactory;
import com.github.syuchan1005.gitprefix.grammar.PrefixResourceFile;
import com.github.syuchan1005.gitprefix.highlight.PrefixResourceSyntaxHighlighter;
import com.github.syuchan1005.gitprefix.ui.EmojiListEditor;
import com.github.syuchan1005.gitprefix.ui.TextAndPreviewEditor;
import com.github.syuchan1005.gitprefix.util.PrefixResourceFileUtil;
import com.intellij.codeInsight.daemon.impl.UpdateHighlightersUtil;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorKind;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.DocumentUtil;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

		Document document = EditorFactory.getInstance().createDocument(PrefixResourceFileUtil.createStructuredFile((PrefixResourceFile) psiFile, true).getText());
		TextEditor structuredPreviewEditor = TextEditorProvider.getInstance()
				.getTextEditor(EditorFactory.getInstance().createViewer(document, project, EditorKind.PREVIEW));
		EditorEx editorEx = EditorUtil.getEditorEx(structuredPreviewEditor);
		if (editorEx != null) {
			editorEx.setHighlighter(EditorHighlighterFactory.getInstance().createEditorHighlighter(project, file.getFileType()));
		}

		mainEditor.getEditor().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void documentChanged(@NotNull DocumentEvent event) {
				PrefixResourceFile prefixResourceFile = PrefixResourceElementFactory.createFile(project, event.getDocument().getText());
				structuredPreviewEditor.getEditor().getDocument()
						.setText(PrefixResourceFileUtil.createStructuredFile(prefixResourceFile, true).getText());
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
