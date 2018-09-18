package com.github.syuchan1005.gitprefix.structureview;

import com.github.syuchan1005.gitprefix.grammar.PrefixResourceFile;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceProperty;
import com.github.syuchan1005.gitprefix.grammar.psi.impl.PrefixResourcePropertyImpl;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.ArrayList;
import java.util.List;
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
			return new PresentationData(property.getKey(), property.getValueText(), property.getIcon(), null);
		}
		return element.getPresentation() != null ? element.getPresentation() : new PresentationData();
	}

	@Override
	public TreeElement[] getChildren() {
		if (element instanceof PrefixResourceFile) {
			PrefixResourceProperty[] properties = PsiTreeUtil.getChildrenOfType(element, PrefixResourceProperty.class);
			if (properties == null) return EMPTY_ARRAY;
			List<TreeElement> treeElements = new ArrayList<>(properties.length);
			for (PrefixResourceProperty property : properties) {
				treeElements.add(new PrefixStructureViewElement((PrefixResourcePropertyImpl) property));
			}
			return treeElements.toArray(new TreeElement[treeElements.size()]);
		}
		return EMPTY_ARRAY;
	}
}
