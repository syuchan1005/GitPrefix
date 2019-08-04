package com.github.syuchan1005.gitprefix.git.injector;

import com.github.syuchan1005.gitprefix.ui.PrefixButton;
import com.github.syuchan1005.gitprefix.util.PrefixResourceFileUtil;
import com.intellij.designer.LightFillLayout;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.JBColor;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBUI;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jetbrains.annotations.NotNull;

public class GitMergeDialogInjector extends AbstractGitDialogInjector {
	public GitMergeDialogInjector(@NotNull Project project) {
		super(project);
	}

	@Override
	public void beforeShow(Object dialog) throws Exception {
		super.beforeShow(dialog);
		JTextField textField = (JTextField) myCenterPanel.getComponent(9);
		PrefixButton prefixButton = new PrefixButton(myProject, new PrefixButton.TextHolder() {
			@Override
			public String getText() {
				String text = textField.getText();
				return text == null ? "" : text;
			}

			@Override
			public void setText(String text) {
				textField.setText(text);
			}
		});
		prefixButton.settingPopup(PrefixResourceFileUtil.BlockType.MERGE);
		if (prefixButton.getPopupMenu() == null) return;
		Object constraintsForComponent = getGridConstraints(9);
		JPanel panel = new JPanel(new LightFillLayout());
		panel.setBorder(JBUI.Borders.customLine(JBColor.BLACK));
		panel.add(prefixButton);
		panel.add(textField);
		myCenterPanel.add(panel, constraintsForComponent, 9);
	}
}
