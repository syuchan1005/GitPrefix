package com.github.syuchan1005.gitprefix.git.commit;

import com.github.syuchan1005.gitprefix.extension.PrefixPanelFactory;
import com.github.syuchan1005.gitprefix.grammar.PrefixResourceFile;
import com.github.syuchan1005.gitprefix.ui.PrefixPanel;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.ui.CommitMessage;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PrefixCheckinHandler extends CheckinHandler {
	private static final ExtensionPointName<PrefixPanelFactory> extensionPointName = new ExtensionPointName<>("com.github.syuchan1005.emojiprefix.prefixPanelFactory");

	private PrefixPanel prefixPanel;
	private CheckinProjectPanel checkinProjectPanel;

	public PrefixCheckinHandler(CheckinProjectPanel checkinProjectPanel) {
		this.checkinProjectPanel = checkinProjectPanel;
		prefixPanel = new PrefixPanel(checkinProjectPanel.getProject());
		if (prefixPanel.notExist()) return;
		try {
			Splitter splitter = (Splitter) checkinProjectPanel.getComponent();
			injectPrefixPanel(splitter);
		} catch (Exception ignored) {
		}
	}

	public CheckinProjectPanel getCheckinProjectPanel() {
		return checkinProjectPanel;
	}

	public void injectPrefixPanel(Splitter splitter) {
		PrefixResourceFile prefixFile = PrefixResourceFile.getFromSetting(checkinProjectPanel.getProject());
		if (prefixFile == null) return;

		CommitMessage commitMessage = (CommitMessage) splitter.getSecondComponent();
		JPanel panel = new JPanel();
		panel.setLayout(new VerticalFlowLayout(true, true));
		JButton test = new JButton("Select Prefix");
		panel.add(test);
		Component commitTextField = commitMessage.getComponent(0);
		panel.add(commitTextField);
		commitMessage.add(panel, 0);

		JBPopupMenu popupMenu = PrefixResourceFile.BlockType.COMMIT.createPopupMenu(prefixFile);
		if (popupMenu == null) return;
		test.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		});

		String comment = commitMessage.getComment();
		String key = PrefixResourceFile.BlockType.COMMIT.containsKey(comment);
		if (key != null) comment = comment.substring(key.length()).trim();
		commitMessage.setCommitMessage(comment);

		for (PrefixPanelFactory factory : extensionPointName.getExtensions()) {
			factory.createPanel(commitMessage);
		}
	}

	@Override
	public ReturnResult beforeCheckin() {
		if (prefixPanel.notExist()) return ReturnResult.COMMIT;
		for (PrefixPanelFactory prefixPanelFactory : extensionPointName.getExtensions()) {
			if (prefixPanelFactory.beforeCheckin() == PrefixPanelFactory.ReturnResult.CANCEL) {
				return ReturnResult.CANCEL;
			}
		}
		String prefix = prefixPanel.getSelectedToolTipText();
		if (prefix != null) {
			checkinProjectPanel.setCommitMessage(prefix + " " + checkinProjectPanel.getCommitMessage());
		}
		return ReturnResult.COMMIT;
	}
}
