package com.github.syuchan1005.gitprefix.git.injector;

import com.github.syuchan1005.gitprefix.ui.PrefixPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Splitter;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import javax.swing.JTextField;
import org.jetbrains.annotations.NotNull;

public class GitMergeDialogInjector extends AbstractGitDialogInjector {
	private JTextField textField;
	private ComboBox comboBox;
	private boolean addPanel = false;

	public GitMergeDialogInjector(@NotNull GitInjectorManager.InjectorType type, @NotNull Project project) {
		super(type, project);
	}

	@Override
	public void beforeShow(Object dialog) throws Exception {
		super.beforeShow(dialog);
		PrefixPanel prefixPanel = new PrefixPanel(myProject);
		if (prefixPanel.notExist()) return;
		textField = (JTextField) myCenterPanel.getComponent(9);
		GridConstraints constraintsForComponent = ((GridLayoutManager) myCenterPanel.getLayout()).getConstraintsForComponent(textField);
		Splitter splitter = new Splitter();
		comboBox = prefixPanel.toComboBox();
		splitter.setFirstComponent(comboBox);
		splitter.setSecondComponent(textField);
		myCenterPanel.add(splitter, constraintsForComponent, 9);
		addPanel = true;
	}

	@Override
	public void afterShow(Object dialog) throws Exception {
		super.afterShow(dialog);
		if (!addPanel) return;
		PrefixPanel.IconTextRadioButton selectedItem = (PrefixPanel.IconTextRadioButton) comboBox.getSelectedItem();
		if (selectedItem == null) return;
		String toolTipText = selectedItem.getRadioButton().getToolTipText();
		if (toolTipText != null) {
			textField.setText(toolTipText + " " + textField.getText());
		}
		addPanel = false;
	}
}
