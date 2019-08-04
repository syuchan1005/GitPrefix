package com.github.syuchan1005.gitprefix.git.injector;

import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceProperty;
import com.github.syuchan1005.gitprefix.ui.PrefixButton;
import com.github.syuchan1005.gitprefix.util.PrefixResourceFileUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;

public class GitTagDialogInjector extends AbstractGitDialogInjector {

	public GitTagDialogInjector(@NotNull Project project) {
		super(project);
	}

	@Override
	public void beforeShow(Object dialog) throws Exception {
		super.beforeShow(dialog);
		JBScrollPane scrollPane = (JBScrollPane) myCenterPanel.getComponent(7);
		Object constraintsForComponent = getGridConstraints(7);
		JPanel panel = new JPanel(new VerticalFlowLayout(true, true));
		JTextArea textArea = (JTextArea) ((JViewport) scrollPane.getComponent(0)).getComponent(0);
		PrefixButton prefixButton = new PrefixButton(myProject, new PrefixButton.TextHolder() {
			@Override
			public String getText() {
				String text = textArea.getText();
				return text == null ? "" : text;
			}

			@Override
			public void setText(String text) {
				textArea.setText(text);
			}
		});
		prefixButton.settingPopup(PrefixResourceFileUtil.BlockType.TAG);
		if (prefixButton.getPopupMenu() == null) return;
		panel.add(prefixButton);
		panel.add(scrollPane);
		myCenterPanel.add(panel, constraintsForComponent, 7);
	}
}
