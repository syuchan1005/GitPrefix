package com.github.syuchan1005.emojiprefix.filetype;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotifications;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmojiResourceFileNotificationProvider extends EditorNotifications.Provider<EditorNotificationPanel> {
	private static final Key<EditorNotificationPanel> KEY = Key.create("EmojiResourceFileNotificationProvider");

	@NotNull
	@Override
	public Key<EditorNotificationPanel> getKey() {
		return KEY;
	}

	@Nullable
	@Override
	public EditorNotificationPanel createNotificationPanel(@NotNull VirtualFile file, @NotNull FileEditor fileEditor) {
		if (!file.getName().equals(".emojirc") ||
				file.getFileType().getName().equals(EmojiResourceFileType.INSTANCE.getName())) return null;
		EditorNotificationPanel editorNotificationPanel = new EditorNotificationPanel();
		editorNotificationPanel.icon(Objects.requireNonNull(EmojiResourceFileType.INSTANCE.getIcon()))
				.text(".emojirc file type is " + file.getFileType().getName() + " now. Do you change file type?");
		editorNotificationPanel.createActionLabel("Yes", () -> {
			FileTypeManager manager = FileTypeManager.getInstance();
			for (FileNameMatcher fileNameMatcher : manager.getAssociations(file.getFileType())) {
				if (fileNameMatcher.accept(".emojirc")) {
					manager.removeAssociation(file.getFileType(), fileNameMatcher);
				}
			}
			editorNotificationPanel.setVisible(false);
		});
		editorNotificationPanel.createActionLabel("No", () -> {
			editorNotificationPanel.setVisible(false);
		});
		return editorNotificationPanel;
	}
}
