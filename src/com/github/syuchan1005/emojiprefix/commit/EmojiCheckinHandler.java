package com.github.syuchan1005.emojiprefix.commit;

import com.github.syuchan1005.emojiprefix.EmojiPanel;
import com.github.syuchan1005.emojiprefix.extension.EmojiPanelFactory;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.ui.CommitMessage;
import java.util.Collections;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * Created by syuchan on 2017/05/29.
 */
public class EmojiCheckinHandler extends CheckinHandler {
	private static final ExtensionPointName<EmojiPanelFactory> extensionPointName = new ExtensionPointName<>("com.github.syuchan1005.emojiprefix.emojiPanelFactory");

	private EmojiPanel emojiPanel;
	private CheckinProjectPanel checkinProjectPanel;

	public EmojiCheckinHandler(CheckinProjectPanel checkinProjectPanel) {
		this.checkinProjectPanel = checkinProjectPanel;
		emojiPanel = new EmojiPanel(checkinProjectPanel.getProject());
		if (emojiPanel.notExists()) return;
		Splitter splitter = (Splitter) checkinProjectPanel.getComponent();
		CommitMessage commitMessage = (CommitMessage) splitter.getSecondComponent();
		Splitter commitSplitter = new Splitter();
		commitSplitter.setFirstComponent(emojiPanel);
		commitSplitter.setSecondComponent((JComponent) commitMessage.getComponent(0));
		commitMessage.add(commitSplitter, 0);
		for (EmojiPanelFactory factory : extensionPointName.getExtensions()) {
			factory.createPanel(commitMessage);
		}
	}

	@Override
	public ReturnResult beforeCheckin() {
		if (emojiPanel.notExists()) return ReturnResult.COMMIT;
		for (EmojiPanelFactory emojiPanelFactory : extensionPointName.getExtensions()) {
			if (emojiPanelFactory.beforeCheckin() == EmojiPanelFactory.ReturnResult.CANCEL) {
				return ReturnResult.CANCEL;
			}
		}
		Collections.list(emojiPanel.getButtonGroup().getElements()).stream().filter(AbstractButton::isSelected).findFirst().ifPresent(button -> {
			String emoji = ((JLabel) button.getComponent(0)).getToolTipText();
			if (emoji != null) {
				checkinProjectPanel.setCommitMessage(emoji + " " + checkinProjectPanel.getCommitMessage());
			}
		});
		return ReturnResult.COMMIT;
	}
}
