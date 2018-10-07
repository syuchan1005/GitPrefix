package com.github.syuchan1005.gitprefix.ui;

import com.github.syuchan1005.gitprefix.EmojiUtil;
import com.github.syuchan1005.gitprefix.grammar.PrefixResourceFile;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceProperty;
import com.github.syuchan1005.gitprefix.util.PrefixResourceFileUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.SmartPsiElementPointer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

public class PrefixButton extends JButton {
	private Project myProject;
	private JBPopupMenu popupMenu;
	private SmartPsiElementPointer<PrefixResourceProperty> current = null;

	public PrefixButton(Project project) {
		this.myProject = project;
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		});
		this.setText("NO PREFIX");
	}

	public void settingPopup(PrefixResourceFileUtil.BlockType type) {
		popupMenu = type.createPopupMenu(PrefixResourceFileUtil.getFromSetting(myProject), (p) -> {
			current = SmartPointerManager.getInstance(p.getProject()).createSmartPsiElementPointer(p);
			EmojiUtil.EmojiData emoji = p.getEmoji();
			if (emoji != null) {
				setIcon(emoji.getIcon());
				setText(p.getValueText());
			} else {
				setIcon(null);
				setText("|" + p.getKey() + "| " + p.getValueText());
			}
		});
		if (popupMenu == null) return;
		JBMenuItem noPrefix = new JBMenuItem("NO PREFIX");
		noPrefix.addActionListener(e -> {
			current = null;
			setText("NO PREFIX");
		});
		popupMenu.add(noPrefix);
	}

	public PrefixResourceProperty getCurrentProperty() {
		if (current == null) return null;
		return current.getElement();
	}

	public SmartPsiElementPointer<PrefixResourceProperty> getCurrent() {
		return current;
	}

	public JBPopupMenu getPopupMenu() {
		return popupMenu;
	}
}
