package com.github.syuchan1005.gitprefix;

import com.github.syuchan1005.gitprefix.psi.PrefixResourceProperty;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.components.JBScrollPane;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class PrefixPanel extends JBScrollPane {
	private static final String NO_PREFIX = "No Prefix";

	private boolean notExists = true;
	private ButtonGroup buttonGroup = new ButtonGroup();

	public PrefixPanel(Project project) {
		JPanel prefixPanel = new JPanel();
		prefixPanel.setLayout(new VerticalFlowLayout(0, 0, 0, true, false));
		VirtualFile gitprefix = project.getBaseDir().findChild(".gitprefix");
		if (gitprefix == null) return;
		PsiFile psiFile = PsiManager.getInstance(project).findFile(gitprefix);
		if (psiFile == null) return;
		PsiElement[] children = psiFile.getChildren();
		if (children.length == 0) return;
		for (PsiElement psiElement : children) {
			if (!(psiElement instanceof PrefixResourceProperty)) continue;
			prefixPanel.add(createPrefixButton(
							psiElement.getFirstChild().getText(),
							psiElement.getFirstChild() == psiElement.getLastChild() ? null : psiElement.getLastChild().getText(),
							false
			));
		}
		prefixPanel.add(createPrefixButton(null, NO_PREFIX, true));
		this.setViewportView(prefixPanel);
		this.setBorder(null);
		notExists = false;
	}

	private IconTextRadioButton createPrefixButton(String text, String description, boolean selected) {
		Icon icon = null;
		if (text != null && text.startsWith(":")) icon = PrefixUtil.getIcon(text.replace(":", ""));

		IconTextRadioButton iconTextRadioButton = new IconTextRadioButton(description, icon, selected);
		buttonGroup.add(iconTextRadioButton.getRadioButton());
		return iconTextRadioButton;
	}

	public boolean notExists() {
		return notExists;
	}

	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	class IconTextRadioButton extends JPanel {
		private JRadioButton radioButton;
		private JLabel label;

		public IconTextRadioButton(String text, Icon icon, boolean selected) {
			super(new FlowLayout(FlowLayout.LEADING, 5, 1));
			this.radioButton = new JRadioButton("", selected);

			this.label = new JLabel(text, icon, SwingConstants.CENTER);
			label.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					radioButton.setSelected(!radioButton.isSelected());
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					radioButton.requestFocus(true);
				}
			});

			this.add(this.radioButton);
			this.add(this.label);
		}

		public void addActionListener(ActionListener var1) {
			this.radioButton.addActionListener(var1);
		}

		public JRadioButton getRadioButton() {
			return radioButton;
		}

		public JLabel getLabel() {
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
}
