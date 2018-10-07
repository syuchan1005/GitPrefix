package com.github.syuchan1005.gitprefix.git.injector;

import com.github.syuchan1005.gitprefix.ui.PrefixButton;
import com.github.syuchan1005.gitprefix.util.PrefixResourceFileUtil;
import com.intellij.openapi.project.Project;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jetbrains.annotations.NotNull;

public class GitMergeDialogInjector extends AbstractGitDialogInjector {
	private JTextField textField;
	private PrefixButton prefixButton;
	private boolean addPanel = false;

	public GitMergeDialogInjector(@NotNull GitInjectorManager.InjectorType type, @NotNull Project project) {
		super(type, project);
	}

	@Override
	public void beforeShow(Object dialog) throws Exception {
		super.beforeShow(dialog);
		textField = (JTextField) myCenterPanel.getComponent(9);
		prefixButton = new PrefixButton(myProject);
		prefixButton.settingPopup(PrefixResourceFileUtil.BlockType.MERGE);
		if (prefixButton.getPopupMenu() == null) return;
		GridConstraints constraintsForComponent = ((GridLayoutManager) myCenterPanel.getLayout()).getConstraintsForComponent(textField);
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(prefixButton);
		panel.add(textField);
		myCenterPanel.add(panel, constraintsForComponent, 9);
		addPanel = true;
	}

	@Override
	public void afterShow(Object dialog) throws Exception {
		super.afterShow(dialog);
		if (!addPanel) return;
		if (prefixButton.getCurrentProperty() == null) return;
		textField.setText(prefixButton.getCurrentProperty().getKey() + " " + textField.getText());
		addPanel = false;
	}
}
