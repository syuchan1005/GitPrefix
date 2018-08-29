package com.github.syuchan1005.gitprefix.git.injector;

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
import org.jetbrains.annotations.NotNull;

public class GitTagDialogInjector extends AbstractGitDialogInjector {
	private JBScrollPane scrollPane;
	private PrefixPanel prefixPanel;
	private boolean addPanel = false;

	public GitTagDialogInjector(@NotNull GitInjectorManager.InjectorType type, @NotNull Project project) {
		super(type, project);
	}

	@Override
	public void beforeShow(Object dialog) throws Exception {
		super.beforeShow(dialog);
		prefixPanel = new PrefixPanel(myProject);
		if (prefixPanel.notExist()) return;
		scrollPane = (JBScrollPane) myCenterPanel.getComponent(7);
		GridConstraints constraintsForComponent = ((GridLayoutManager) myCenterPanel.getLayout()).getConstraintsForComponent(scrollPane);
		Splitter splitter = new Splitter();
		splitter.setFirstComponent(prefixPanel);
		splitter.setSecondComponent(scrollPane);
		myCenterPanel.add(splitter, constraintsForComponent, 7);
		addPanel = true;
	}

	@Override
	public void afterShow(Object dialog) throws Exception {
		super.afterShow(dialog);
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
}
