package com.github.syuchan1005.gitprefix.filetype;

import a.h.b.E;
import com.intellij.ide.IdeView;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;

public class PrefixNewFileAction extends AnAction {
	@Override
	public void actionPerformed(AnActionEvent e) {
		Project project = e.getProject();
		if (project == null) return;
		IdeView ideView = e.getRequiredData(LangDataKeys.IDE_VIEW);
		PsiDirectory directory = ideView.getOrChooseDirectory();
		if (directory != null) {
			VirtualFile file = null;
			String ext = "." + PrefixResourceFileType.DEFAULT_EXTENSION;
			try {
				directory.checkCreateFile(ext);
				file = directory.createFile(ext).getVirtualFile();
			} catch (IncorrectOperationException ignored) {
				Notifications.Bus.notify(new Notification("GitPrefix", "Create File", ext + " already exists", NotificationType.INFORMATION));
				PsiFile psiFile = directory.findFile(ext);
				if (psiFile != null) file = psiFile.getVirtualFile();
			}
			if (file != null)  FileEditorManager.getInstance(project).openFile(file, true);
		}
	}

	@Override
	public void update(AnActionEvent e) {
		Project project = e.getProject();
		IdeView ideView;
		try {
			ideView = e.getRequiredData(LangDataKeys.IDE_VIEW);
		} catch (Throwable ignored) {
			return;
		}
		PsiDirectory[] directories = ideView.getDirectories();
		if (directories.length == 0 || project == null) {
			e.getPresentation().setVisible(false);
		}
	}
}
