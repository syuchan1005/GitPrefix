package com.github.syuchan1005.gitprefix.filetype;

import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;

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
			WriteCommandAction.runWriteCommandAction(project, () -> {
				directory.createFile(ext);
			});
			PsiFile psiFile = directory.findFile(ext);
			if (psiFile != null) file = psiFile.getVirtualFile();
			if (file != null) FileEditorManager.getInstance(project).openFile(file, true);
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
