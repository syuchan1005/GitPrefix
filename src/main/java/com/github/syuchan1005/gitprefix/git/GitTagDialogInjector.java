package com.github.syuchan1005.gitprefix.git;

import com.github.syuchan1005.gitprefix.EmojiUtil;
import com.github.syuchan1005.gitprefix.ui.PrefixPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Splitter;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import git4idea.ui.GitTagDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class GitTagDialogInjector extends AbstractGitDialogInjector {
	private static JBScrollPane scrollPane;
	private static PrefixPanel prefixPanel;
	private static boolean addPanel = false;

	public static void beforeShow(GitTagDialog dialog) throws Exception {
		Project project = GitInjectorUtil.getProject(dialog);
		prefixPanel = new PrefixPanel(project);
		if (prefixPanel.notExist()) return;
		JPanel panel = GitInjectorUtil.getPanel(dialog);
		scrollPane = (JBScrollPane) panel.getComponent(7);
		GridConstraints constraintsForComponent = ((GridLayoutManager) panel.getLayout()).getConstraintsForComponent(scrollPane);
		Splitter splitter = new Splitter();
		splitter.setFirstComponent(prefixPanel);
		splitter.setSecondComponent(scrollPane);
		panel.add(splitter, constraintsForComponent, 7);
		addPanel = true;
	}

	public static void afterShow(GitTagDialog dialog) {
		if (!addPanel) return;
		String toolTipText = prefixPanel.getSelectedToolTipText();
		if (toolTipText != null) {
			JTextArea textArea = (JTextArea) scrollPane.getComponent(0).getComponentAt(0, 0);
			if (toolTipText.charAt(0) == ':' && toolTipText.charAt(toolTipText.length() - 1) == ':') {
				String emoji = toolTipText.substring(1, toolTipText.length() - 2);
				EmojiUtil.EmojiData emojiData = EmojiUtil.getEmojiData(emoji);
				if (emojiData != null) toolTipText = emojiData.getCode();
			}
			textArea.setText(toolTipText + " " + textArea.getText());
		}
		addPanel = false;
	}

	@Override
	public String getInjectClassName() {
		return "git4idea.ui.GitTagDialog";
	}
}
