package com.github.syuchan1005.gitprefix.filetype;

import com.intellij.icons.AllIcons;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.BalloonImpl;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.messages.MessageBusConnection;
import java.io.IOException;
import javax.swing.BorderFactory;
import org.jetbrains.annotations.NotNull;

public class EmojiResourceRenameNotifier extends AbstractProjectComponent {
	private Project project;

	protected EmojiResourceRenameNotifier(Project project) {
		super(project);
		this.project = project;
	}

	@Override
	public void projectOpened() {
		MessageBusConnection connection = project.getMessageBus().connect(project);
		connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListener() {
			@Override
			public void fileOpened(@NotNull FileEditorManager manager, @NotNull VirtualFile file) {
				if (file.getFileType() instanceof EmojiResourceFileType) {
					Notification notification = new Notification("GitPrefix", "Old extension detected.",
									"Change to a new extension(.gitprefix)?",
									NotificationType.WARNING);
					notification.addAction(new AnAction("Yes") {
						@Override
						public void actionPerformed(AnActionEvent anActionEvent) {
							try {
								file.rename(project, "." + PrefixResourceFileType.DEFAULT_EXTENSION);
							} catch (IOException ignored) {}
						}
					});
					notification.addAction(new AnAction("No") {
						@Override
						public void actionPerformed(AnActionEvent anActionEvent) {
							notification.hideBalloon();
						}
					});
					Notifications.Bus.notify(notification);
				}
			}
		});
	}
}
