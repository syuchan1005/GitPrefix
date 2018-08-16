package com.github.syuchan1005.gitprefix;

import com.github.syuchan1005.gitprefix.psi.PrefixResourceProperty;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PrefixPanel extends JBScrollPane {
	private static final String NO_PREFIX = "No Prefix";

	private ButtonGroup buttonGroup = new ButtonGroup();
	private Project myProject;

	public PrefixPanel(Project project) {
		this.myProject = project;
		JPanel prefixPanel = new JPanel();
		prefixPanel.setLayout(new VerticalFlowLayout(0, 0, 0, true, false));
		PsiElement[] children = PrefixPanel.getGitPrefixFilePsiElements(myProject);
		if (children == null) return;
		for (PsiElement psiElement : children) {
			if (!(psiElement instanceof PrefixResourceProperty)) continue;
			IconTextRadioButton prefixButton = createPrefixButton(
							psiElement.getFirstChild().getText(),
							psiElement.getFirstChild() == psiElement.getLastChild() ? null : psiElement.getLastChild().getText(),
							false
			);
			prefixPanel.add(prefixButton);
		}
		prefixPanel.add(createPrefixButton(null, NO_PREFIX, true));
		this.setViewportView(prefixPanel);
		this.setBorder(null);
	}

	private IconTextRadioButton createPrefixButton(String text, String description, boolean selected) {
		Icon icon = null;
		if (text != null && text.startsWith(":")) icon = PrefixUtil.getIcon(text.replace(":", ""));

		IconTextRadioButton iconTextRadioButton = new IconTextRadioButton(description, icon, selected);
		iconTextRadioButton.getRadioButton().setToolTipText(text);
		buttonGroup.add(iconTextRadioButton.getRadioButton());
		return iconTextRadioButton;
	}

	public boolean notExist() {
		return PrefixPanel.getGitPrefixFilePsiElements(myProject) == null;
	}

	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	class IconTextRadioButton extends JPanel {
		private JRadioButton radioButton;
		private JLabel label;

		private IconTextRadioButton(String text, Icon icon, boolean selected) {
			super(new FlowLayout(FlowLayout.LEADING, 5, 1));
			this.radioButton = new JRadioButton("", selected);

			this.label = new JLabel(text, icon, SwingConstants.CENTER);
			label.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					radioButton.setSelected(!radioButton.isSelected());
				}
			});

			this.add(this.radioButton);
			this.add(this.label);
		}

		public void addActionListener(ActionListener var1) {
			this.radioButton.addActionListener(var1);
		}

		private JRadioButton getRadioButton() {
			return radioButton;
		}

		private JLabel getLabel() {
			return label;
		}

		public boolean isSameAs(Object var1) {
			return this.radioButton == var1;
		}

		public void setEnabled(boolean var1) {
			this.radioButton.setEnabled(var1);
			this.label.setEnabled(var1);
		}

		public boolean isSelected() {
			return this.radioButton.isSelected();
		}

		public void setSelected(boolean var1) {
			this.radioButton.setSelected(var1);
		}
	}

	@Nullable
	public static PsiElement[] getGitPrefixFilePsiElements(Project project) {
		GitPrefixData prefixData = ServiceManager.getService(project, GitPrefixData.class);
		VirtualFile virtualFile;
		if (prefixData.getIsPathType().equals("DEFAULT")) {
			virtualFile = project.getBaseDir().findChild(".gitprefix");
		} else if (prefixData.getIsPathType().equals("CUSTOM") && !prefixData.getGitPrefixPath().equals("")) {
			virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(prefixData.getGitPrefixPath());
		} else {
			return null;
		}
		if (virtualFile == null) return null;
		PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
		if (psiFile == null) return null;
		PsiElement[] children = psiFile.getChildren();
		if (children.length == 0) return null;
		return children;
	}
}
