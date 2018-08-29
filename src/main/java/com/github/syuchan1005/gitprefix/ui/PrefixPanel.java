package com.github.syuchan1005.gitprefix.ui;

import com.github.syuchan1005.gitprefix.EmojiUtil;
import com.github.syuchan1005.gitprefix.GitPrefixData;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.components.JBScrollPane;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrefixPanel extends JBScrollPane {
	private static final String NO_PREFIX = "No Prefix";

	private ButtonGroup buttonGroup = new ButtonGroup();
	private Project myProject;

	public PrefixPanel(Project project) {
		this.myProject = project;
		JPanel prefixPanel = new JPanel();
		prefixPanel.setLayout(new VerticalFlowLayout(0, 0, 0, true, false));
		PsiElement[] children = PrefixPanel.getGitPrefixFilePsiElements(myProject);
		if (children == null) return;
		for (PsiElement psiElement : children) {
			if (!psiElement.getClass().getSimpleName().equals("PrefixResourcePropertyImpl")) continue;
			IconTextRadioButton prefixButton = createPrefixButton(
					psiElement.getFirstChild().getText(),
					psiElement.getFirstChild() == psiElement.getLastChild() ? null : psiElement.getLastChild().getText(),
					false
			);
			prefixPanel.add(prefixButton);
		}
		prefixPanel.add(createPrefixButton(null, NO_PREFIX, true));
		this.setViewportView(prefixPanel);
		this.setBorder(null);
	}

	public ComboBox toComboBox() {
		ComboBox<JPanel> comboBox = new ComboBox<>();
		JPanel prefixPanel = (JPanel) this.getViewport().getView();
		for (Component component : prefixPanel.getComponents()) {
			comboBox.addItem((JPanel) component);
		}
		comboBox.setRenderer(new PrefixComboBoxRenderer());
		comboBox.setSelectedIndex(comboBox.getItemCount() - 1);
		return comboBox;
	}

	@Nullable
	public static PsiElement[] getGitPrefixFilePsiElements(Project project) {
		GitPrefixData prefixData = toData(ServiceManager.getService(project, GitPrefixData.class));
		VirtualFile virtualFile;
		if (prefixData.isPathType.equals("DEFAULT")) {
			virtualFile = project.getBaseDir().findChild(".gitprefix");
		} else if (prefixData.isPathType.equals("CUSTOM") && !prefixData.gitPrefixPath.equals("")) {
			virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(prefixData.gitPrefixPath);
		} else {
			return null;
		}
		if (virtualFile == null) return null;
		PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
		if (psiFile == null) return null;
		PsiElement[] children = psiFile.getChildren();
		if (children.length == 0) return null;
		return children;
	}

	/**
	 * ClassLoaderA->GitPrefixData cast to ClassLoaderB->GitPrefixData
	 *
	 * @param object {@link com.github.syuchan1005.gitprefix.GitPrefixData}
	 * @return {@link com.github.syuchan1005.gitprefix.GitPrefixData}
	 */
	private static GitPrefixData toData(Object object) {
		Class<?> clazz = object.getClass();
		try {
			String isPathType = (String) clazz.getField("isPathType").get(object);
			String gitPrefixPath = (String) clazz.getField("gitPrefixPath").get(object);
			return new GitPrefixData(isPathType, gitPrefixPath);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
			return new GitPrefixData();
		}
	}

	private IconTextRadioButton createPrefixButton(String text, String description, boolean selected) {
		EmojiUtil.EmojiData emojiData = null;
		if (text != null && text.startsWith(":")) emojiData = EmojiUtil.getEmojiData(text.replace(":", ""));
		IconTextRadioButton iconTextRadioButton = new IconTextRadioButton(description, emojiData != null ? emojiData.getIcon() : null, selected);
		iconTextRadioButton.getRadioButton().setToolTipText(text);
		buttonGroup.add(iconTextRadioButton.getRadioButton());
		return iconTextRadioButton;
	}

	public boolean notExist() {
		return PrefixPanel.getGitPrefixFilePsiElements(myProject) == null;
	}

	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	public String getSelectedToolTipText() {
		Enumeration<AbstractButton> elements = buttonGroup.getElements();
		while (elements.hasMoreElements()) {
			AbstractButton abstractButton = elements.nextElement();
			if (abstractButton.isSelected()) return abstractButton.getToolTipText();
		}
		return null;
	}

	public static class IconTextRadioButton extends JPanel {
		private JRadioButton radioButton;
		private JLabel label;

		private IconTextRadioButton(String text, Icon icon, boolean selected) {
			super(new FlowLayout(FlowLayout.LEADING, 5, 1));
			this.radioButton = new JRadioButton("", selected);

			this.label = new JLabel(text, icon, SwingConstants.CENTER);
			label.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					radioButton.setSelected(!radioButton.isSelected());
				}
			});

			this.add(this.radioButton);
			this.add(this.label);
		}

		public void addActionListener(ActionListener var1) {
			this.radioButton.addActionListener(var1);
		}

		public JRadioButton getRadioButton() {
			return radioButton;
		}

		public JLabel getLabel() {
			return label;
		}

		public boolean isSameAs(Object var1) {
			return this.radioButton == var1;
		}

		public void setEnabled(boolean var1) {
			this.radioButton.setEnabled(var1);
			this.label.setEnabled(var1);
		}

		public boolean isSelected() {
			return this.radioButton.isSelected();
		}

		public void setSelected(boolean var1) {
			this.radioButton.setSelected(var1);
		}
	}

	static class PrefixComboBoxRenderer extends ColoredListCellRenderer<JPanel> {
		@Override
		public Component getListCellRendererComponent(JList list, JPanel value, int index, boolean isSelected, boolean cellHasFocus) {
			IconTextRadioButton button = (IconTextRadioButton) value;
			JLabel label = button.getLabel();
			label.setHorizontalAlignment(SwingConstants.LEFT);

			if (isSelected) {
				label.setBackground(list.getSelectionBackground());
				label.setForeground(list.getSelectionForeground());
			} else {
				label.setBackground(list.getBackground());
				label.setForeground(list.getForeground());
			}

			return label;
		}

		@Override
		protected void customizeCellRenderer(@NotNull JList<? extends JPanel> list, JPanel value, int index, boolean selected, boolean hasFocus) {
		}
	}
}
