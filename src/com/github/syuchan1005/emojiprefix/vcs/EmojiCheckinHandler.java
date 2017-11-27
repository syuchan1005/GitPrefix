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
import java.util.Collections;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

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
			String emoji = psiElement.getFirstChild().getText();
			JRadioButton radioButton = new JRadioButton(emoji + " " + psiElement.getLastChild().getText());
			radioButton.setToolTipText(emoji);
			buttonGroup.add(radioButton);
			emojiPanel.add(radioButton);
		}
		JRadioButton noRadio = new JRadioButton(NO_EMOJI, true);
		buttonGroup.add(noRadio);
		emojiPanel.add(noRadio);
		this.checkinProjectPanel = checkinProjectPanel;
	}

	@Override
	public ReturnResult beforeCheckin() {
		if (checkinProjectPanel == null) return ReturnResult.COMMIT;
		DialogBuilder dialogBuilder = new DialogBuilder(checkinProjectPanel.getProject());
		dialogBuilder.setTitle("Emoji Select");
		int i = dialogBuilder
				.centerPanel(new JBScrollPane(emojiPanel))
				.okActionEnabled(true)
				.show();
		if (i != 0) {
			return ReturnResult.CANCEL;
		} else {
			Collections.list(buttonGroup.getElements()).stream().filter(AbstractButton::isSelected).findFirst().ifPresent(button -> {
				if (!button.getText().equals(NO_EMOJI)) {
					checkinProjectPanel.setCommitMessage(button.getToolTipText() + " " + checkinProjectPanel.getCommitMessage());
				}
			});
			return ReturnResult.COMMIT;
		}
	}
}
