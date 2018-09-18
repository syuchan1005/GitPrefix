package com.github.syuchan1005.gitprefix.git.commit;

import com.github.syuchan1005.gitprefix.extension.PrefixPanelFactory;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceProperty;
import com.github.syuchan1005.gitprefix.ui.PrefixPanel;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.ui.CommitMessage;
import com.intellij.psi.PsiElement;
import java.util.Arrays;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.JComponent;

/**
 * Created by syuchan on 2017/05/29.
 */
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
		PsiElement[] psiElements = PrefixPanel.getGitPrefixFilePsiElements(checkinProjectPanel.getProject());
		if (psiElements == null) return;
		String[] keys = Arrays.stream(psiElements)
				.filter(e -> e instanceof PrefixResourceProperty)
				.map(p -> p.getFirstChild().getText())
				.sorted((s1, s2) -> {
					int sub = s2.length() - s1.length();
					if (sub != 0) return sub;
					return s1.compareTo(s2);
				}).toArray(String[]::new);

		CommitMessage commitMessage = (CommitMessage) splitter.getSecondComponent();
		Splitter commitSplitter = new Splitter();
		commitSplitter.setFirstComponent(prefixPanel);
		commitSplitter.setSecondComponent((JComponent) commitMessage.getComponent(0));
		commitMessage.add(commitSplitter, 0);

		String comment = commitMessage.getComment();
		for (String key : keys) {
			if (comment.startsWith(key)) {
				Enumeration<AbstractButton> elements = prefixPanel.getButtonGroup().getElements();
				while (elements.hasMoreElements()) {
					AbstractButton button = elements.nextElement();
					if (button.getToolTipText().equals(key)) {
						button.setSelected(true);
						String raw = comment.substring(key.length());
						commitMessage.setCommitMessage(raw.length() > 0 && raw.charAt(0) == ' ' ? raw.substring(1) : raw);
						break;
					}
				}
				break;
			}
		}

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
