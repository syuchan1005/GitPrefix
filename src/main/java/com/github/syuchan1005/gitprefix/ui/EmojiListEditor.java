package com.github.syuchan1005.gitprefix.ui;

import com.github.syuchan1005.gitprefix.EmojiUtil;
import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.util.Key;
import com.intellij.ui.components.JBLabel;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmojiListEditor implements FileEditor {
	private JPanel panel;
	private EmojiTable emojiTable;
	private JBLabel copiedLabel;

	private void createUIComponents() {
		List<Object[]> objects = new ArrayList<>();
		for (Map.Entry<String, EmojiUtil.EmojiData> entry : EmojiUtil.getEmojiMap().entrySet()) {
			objects.add(new Object[]{entry.getValue(), entry.getKey()});
		}
		emojiTable = new EmojiTable(this, objects.toArray(new Object[objects.size()][]));
	}

	public JBLabel getCopiedLabel() {
		return copiedLabel;
	}

	@NotNull
	@Override
	public JComponent getComponent() {
		return panel;
	}

	@Nullable
	@Override
	public JComponent getPreferredFocusedComponent() {
		return null;
	}

	@NotNull
	@Override
	public String getName() {
		return "EmojiListEditor";
	}

	@Override
	public void setState(@NotNull FileEditorState state) {

	}

	@Override
	public boolean isModified() {
		return false;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public void selectNotify() {
	}

	@Override
	public void deselectNotify() {
	}

	@Override
	public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
	}

	@Override
	public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
	}

	@Nullable
	@Override
	public BackgroundEditorHighlighter getBackgroundHighlighter() {
		return null;
	}

	@Nullable
	@Override
	public FileEditorLocation getCurrentLocation() {
		return null;
	}

	@Override
	public void dispose() {
	}

	@Nullable
	@Override
	public <T> T getUserData(@NotNull Key<T> key) {
		return null;
	}

	@Override
	public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {
	}
}
