package com.github.syuchan1005.emojiprefix;

import com.github.syuchan1005.emojiprefix.psi.EmojiResourceProperty;
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

public class EmojiPanel extends JBScrollPane {
	private static final String NO_EMOJI = "No Emoji";

	private boolean notExists = true;
	private ButtonGroup buttonGroup = new ButtonGroup();

	public EmojiPanel(Project project) {
		JPanel emojiPanel = new JPanel();
		emojiPanel.setLayout(new VerticalFlowLayout());
		VirtualFile emojirc = project.getBaseDir().findChild(".emojirc");
		if (emojirc == null) return;
		PsiFile psiFile = PsiManager.getInstance(project).findFile(emojirc);
		if (psiFile == null) return;
		int count = 0;
		for (PsiElement psiElement : psiFile.getChildren()) {
			if (!(psiElement instanceof EmojiResourceProperty)) continue;
			emojiPanel.add(createEmojiButton(psiElement.getFirstChild().getText(), psiElement.getLastChild().getText(), false));
			count++;
		}
		if (count == 0) return;
		emojiPanel.add(createEmojiButton(null, NO_EMOJI, true));
		this.setViewportView(emojiPanel);
		this.setBorder(null);
		notExists = false;
	}

	private JRadioButton createEmojiButton(String emoji, String description, boolean selected) {
		JRadioButton radioButton = new JRadioButton("", selected);
		buttonGroup.add(radioButton);
		int space = UIManager.getIcon("RadioButton.icon").getIconWidth() + radioButton.getIconTextGap();
		JLabel iconLabel;
		if (emoji != null) {
			iconLabel = new JLabel(description, EmojiUtil.getIcon(emoji.replace(":", "")), SwingConstants.CENTER);
		} else {
			iconLabel = new JLabel(description);
		}
		iconLabel.setToolTipText(emoji);
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
