package com.github.syuchan1005.gitprefix.setting;

import com.github.syuchan1005.gitprefix.GitPrefixData;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class GitPrefixConfigurableGUI {
	private JPanel rootPane;
	private JRadioButton defaultRadioButton;
	private JRadioButton customRadioButton;
	private TextFieldWithBrowseButton pathField;

	private GitPrefixData.PathType initPathType;
	private String initFilePath;

	public GitPrefixConfigurableGUI(Project project, GitPrefixData.PathType pathType, String filePath) {
		this.initPathType = pathType;
		this.initFilePath = filePath;

		if (pathType == GitPrefixData.PathType.DEFAULT) {
			defaultRadioButton.setSelected(true);
		} else {
			customRadioButton.setSelected(true);
		}
		if (filePath != null) pathField.setText(filePath);

		defaultRadioButton.addActionListener(e -> pathField.setEnabled(customRadioButton.isSelected()));
		customRadioButton.addActionListener(e -> pathField.setEnabled(customRadioButton.isSelected()));
		pathField.setEnabled(customRadioButton.isSelected());

		pathField.addBrowseFolderListener(null, null, project,
				FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor().withShowHiddenFiles(true));
	}

	public boolean isModified() {
		return !(defaultRadioButton.isSelected() && this.initPathType == GitPrefixData.PathType.DEFAULT) || !this.initFilePath.equals(pathField.getText());
	}

	public JPanel getRootPane() {
		return rootPane;
	}

	public JRadioButton getDefaultRadioButton() {
		return defaultRadioButton;
	}

	public TextFieldWithBrowseButton getPathField() {
		return pathField;
	}
}
