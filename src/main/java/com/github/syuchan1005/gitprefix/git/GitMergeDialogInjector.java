package com.github.syuchan1005.gitprefix.git;

import com.github.syuchan1005.gitprefix.ui.PrefixPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Splitter;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import git4idea.merge.GitMergeDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GitMergeDialogInjector extends AbstractGitDialogInjector {
	private static JTextField textField;
	private static ComboBox comboBox;
	private static boolean addPanel = false;

	public static void beforeShow(GitMergeDialog dialog) throws Exception {
		Project project = GitInjectorUtil.getProject(dialog);
		PrefixPanel prefixPanel = new PrefixPanel(project);
		if (prefixPanel.notExist()) return;
		JPanel panel = GitInjectorUtil.getPanel(dialog);
		textField = (JTextField) panel.getComponent(9);
		GridConstraints constraintsForComponent = ((GridLayoutManager) panel.getLayout()).getConstraintsForComponent(textField);
		Splitter splitter = new Splitter();
		comboBox = prefixPanel.toComboBox();
		splitter.setFirstComponent(comboBox);
		splitter.setSecondComponent(textField);
		panel.add(splitter, constraintsForComponent, 9);
		addPanel = true;
	}

	public static void afterShow(GitMergeDialog dialog) {
		if (!addPanel) return;
		PrefixPanel.IconTextRadioButton selectedItem = (PrefixPanel.IconTextRadioButton) comboBox.getSelectedItem();
		if (selectedItem == null) return;
		String toolTipText = selectedItem.getRadioButton().getToolTipText();
		if (toolTipText != null) {
			textField.setText(toolTipText + " " + textField.getText());
		}
		addPanel = false;
	}

	@Override
	public String getInjectClassName() {
		return "git4idea.merge.GitMergeDialog";
	}
}
