package com.github.syuchan1005.emojiprefix.vcs;

import com.github.syuchan1005.emojiprefix.EmojiUtil;
import com.github.syuchan1005.emojiprefix.psi.EmojiResourceProperty;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.UIUtil;
import java.util.Collections;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * Created by syuchan on 2017/05/29.
 */
public class EmojiCheckinHandler extends CheckinHandler {
	private static final String NO_EMOJI = "No Emoji";

	private JPanel emojiPanel;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private CheckinProjectPanel checkinProjectPanel = null;

	public EmojiCheckinHandler(CheckinProjectPanel checkinProjectPanel) {
		emojiPanel = new JPanel();
		emojiPanel.setLayout(new VerticalFlowLayout());
		VirtualFile emojirc = checkinProjectPanel.getProject().getBaseDir().findChild(".emojirc");
		if (emojirc == null) return;
		PsiFile psiFile = PsiManager.getInstance(checkinProjectPanel.getProject()).findFile(emojirc);
		if (psiFile == null) return;
		for (PsiElement psiElement : psiFile.getChildren()) {
			if (!(psiElement instanceof EmojiResourceProperty)) continue;
			emojiPanel.add(createEmojiButton(psiElement.getFirstChild().getText(), psiElement.getLastChild().getText(), false, buttonGroup));
		}
		emojiPanel.add(createEmojiButton(null, NO_EMOJI, true, buttonGroup));
		this.checkinProjectPanel = checkinProjectPanel;
	}

	@Override
	public ReturnResult beforeCheckin() {
		if (checkinProjectPanel == null) return ReturnResult.COMMIT;
		DialogBuilder dialogBuilder = new DialogBuilder(checkinProjectPanel.getProject());
		dialogBuilder.setTitle("Emoji Select");
		boolean isOk = dialogBuilder
				.centerPanel(new JBScrollPane(emojiPanel))
				.okActionEnabled(true)
				.show() == 0;
		if (isOk) {
			Collections.list(buttonGroup.getElements()).stream().filter(AbstractButton::isSelected).findFirst().ifPresent(button -> {
				String emoji = ((JLabel) button.getComponent(0)).getToolTipText();
				if (emoji != null) {
					checkinProjectPanel.setCommitMessage(emoji + " " + checkinProjectPanel.getCommitMessage());
				}
			});
			return ReturnResult.COMMIT;
		} else {
			return ReturnResult.CANCEL;
		}
	}

	private static JRadioButton createEmojiButton(String emoji, String description, boolean selected, ButtonGroup buttonGroup) {
		JRadioButton radioButton = new JRadioButton("", selected);
		buttonGroup.add(radioButton);
		int leftBorder = (int) (UIManager.getIcon("RadioButton.icon").getIconWidth() * 1.5);
		EmptyBorder border = new EmptyBorder(0, leftBorder, 0, 0);
		if (emoji != null) {
			JLabel iconLabel = new JLabel(EmojiUtil.getIcon(emoji.replace(":", "")));
			iconLabel.setToolTipText(emoji);
			iconLabel.setBorder(border);
			border = null;
			leftBorder += (UIUtil.isRetina() ? 32 : 16) + 5;
			radioButton.add(iconLabel);
		}
		JLabel label = new JLabel(description);
		if (border != null) label.setBorder(border);
		else label.setBorder(new EmptyBorder(0, leftBorder, 0, 0));
		radioButton.add(label);
		return radioButton;
	}
}
