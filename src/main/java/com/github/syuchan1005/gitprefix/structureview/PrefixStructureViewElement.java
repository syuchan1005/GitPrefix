package com.github.syuchan1005.gitprefix.structureview;

import com.github.syuchan1005.gitprefix.EmojiUtil;
import com.github.syuchan1005.gitprefix.psi.PrefixResourceFile;
import com.github.syuchan1005.gitprefix.psi.PrefixResourceProperty;
import com.github.syuchan1005.gitprefix.psi.impl.PrefixResourcePropertyImpl;
import com.intellij.icons.AllIcons;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

public class PrefixStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
	private NavigatablePsiElement element;

	public PrefixStructureViewElement(NavigatablePsiElement element) {
		this.element = element;
	}

	@Override
	public Object getValue() {
		return element;
	}

	@Override
	public void navigate(boolean b) {
		element.navigate(b);
	}

	@Override
	public boolean canNavigate() {
		return element.canNavigate();
	}

	@Override
	public boolean canNavigateToSource() {
		return element.canNavigateToSource();
	}

	@NotNull
	@Override
	public String getAlphaSortKey() {
		String name = element.getName();
		return name != null ? name : "none";
	}

	@NotNull
	@Override
	public ItemPresentation getPresentation() {
		if (element instanceof PrefixResourceProperty) {
			PrefixResourceProperty property = (PrefixResourceProperty) this.element;
			String text = property.getFirstChild().getText();
			Icon icon = null;
			if (text.charAt(0) == ':' && text.charAt(text.length() - 1) == ':') {
				icon = EmojiUtil.getIcon(text.substring(1, text.length() - 1));
			}
			return new PresentationData(text, property.getLastChild().getText(), icon == null ? AllIcons.Nodes.Field : icon, null);
		}
		return element.getPresentation() != null ? element.getPresentation() : new PresentationData();
	}

	@Override
	public TreeElement[] getChildren() {
		if (element instanceof PrefixResourceFile) {
			PrefixResourceProperty[] properties = PsiTreeUtil.getChildrenOfType(element, PrefixResourceProperty.class);
			List<TreeElement> treeElements = new ArrayList<>(properties.length);
			for (PrefixResourceProperty property : properties) {
				treeElements.add(new PrefixStructureViewElement((PrefixResourcePropertyImpl) property));
			}
			return treeElements.toArray(new TreeElement[treeElements.size()]);
		} else {
			return EMPTY_ARRAY;
		}
	}
}
