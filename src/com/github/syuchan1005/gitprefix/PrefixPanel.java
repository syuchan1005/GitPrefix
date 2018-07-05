package com.github.syuchan1005.gitprefix;

import com.github.syuchan1005.gitprefix.psi.PrefixResourceProperty;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class PrefixPanel extends JBScrollPane {
	private static final String NO_PREFIX = "No Prefix";

	private boolean notExists = true;
	private ButtonGroup buttonGroup = new ButtonGroup();

	public PrefixPanel(Project project) {
		JPanel prefixPanel = new JPanel();
		prefixPanel.setLayout(new VerticalFlowLayout());
		VirtualFile gitprefix = project.getBaseDir().findChild(".gitprefix");
		if (gitprefix == null) return;
		PsiFile psiFile = PsiManager.getInstance(project).findFile(gitprefix);
		if (psiFile == null) return;
		int count = 0;
		for (PsiElement psiElement : psiFile.getChildren()) {
			if (!(psiElement instanceof PrefixResourceProperty)) continue;
			prefixPanel.add(createPrefixButton(psiElement.getFirstChild().getText(),
					psiElement.getFirstChild() == psiElement.getLastChild() ? null : psiElement.getLastChild().getText(), false));
			count++;
		}
		if (count == 0) return;
		prefixPanel.add(createPrefixButton(null, NO_PREFIX, true));
		this.setViewportView(prefixPanel);
		this.setBorder(null);
		notExists = false;
	}

	private JRadioButton createPrefixButton(String text, String description, boolean selected) {
		JRadioButton radioButton = new JRadioButton("", selected);
		buttonGroup.add(radioButton);
		int space = UIManager.getIcon("RadioButton.icon").getIconWidth() + radioButton.getIconTextGap();
		JLabel iconLabel;
		if (text != null) {
			if (text.startsWith(":")) {
				iconLabel = new JLabel(description, PrefixUtil.getIcon(text.replace(":", "")), SwingConstants.CENTER);
			} else {
				iconLabel = new JLabel(text.substring(1, text.length() - 1) + (description == null ? "" : " | " + description));
			}
		} else {
			iconLabel = new JLabel(description);
		}
		iconLabel.setToolTipText(text);
		iconLabel.setBorder(JBUI.Borders.emptyLeft(space));
		radioButton.add(iconLabel);
		iconLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				radioButton.setSelected(true);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				radioButton.requestFocus(true);
			}
		});
		return radioButton;
	}

	public boolean notExists() {
		return notExists;
	}

	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}
}
