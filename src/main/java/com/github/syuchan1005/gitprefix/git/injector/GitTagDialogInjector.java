package com.github.syuchan1005.gitprefix.git.injector;

import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceProperty;
import com.github.syuchan1005.gitprefix.ui.PrefixButton;
import com.github.syuchan1005.gitprefix.util.PrefixResourceFileUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.jetbrains.annotations.NotNull;

public class GitTagDialogInjector extends AbstractGitDialogInjector {
	private JBScrollPane scrollPane;
	private PrefixButton prefixButton;
	private boolean addPanel = false;

	public GitTagDialogInjector(@NotNull GitInjectorManager.InjectorType type, @NotNull Project project) {
		super(type, project);
	}

	@Override
	public void beforeShow(Object dialog) throws Exception {
		super.beforeShow(dialog);
		scrollPane = (JBScrollPane) myCenterPanel.getComponent(7);
		GridConstraints constraintsForComponent = ((GridLayoutManager) myCenterPanel.getLayout()).getConstraintsForComponent(scrollPane);
		JPanel panel = new JPanel(new VerticalFlowLayout(true, true));
		prefixButton = new PrefixButton(myProject);
		prefixButton.settingPopup(PrefixResourceFileUtil.BlockType.TAG);
		if (prefixButton.getPopupMenu() == null) return;
		panel.add(prefixButton);
		panel.add(scrollPane);
		myCenterPanel.add(panel, constraintsForComponent, 7);
		addPanel = true;
	}

	@Override
	public void afterShow(Object dialog) throws Exception {
		super.afterShow(dialog);
		if (!addPanel) return;
		PrefixResourceProperty currentProperty = prefixButton.getCurrentProperty();
		if (currentProperty != null) {
			JTextArea textArea = (JTextArea) scrollPane.getComponent(0).getComponentAt(0, 0);
			textArea.setText(currentProperty.getKey() + " " + textArea.getText());
		}
		addPanel = false;
	}
}
