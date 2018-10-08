package com.github.syuchan1005.gitprefix.structureview;

import com.github.syuchan1005.gitprefix.grammar.PrefixResourceFile;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceBlockExpr;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceNamedBlock;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceProperty;
import com.github.syuchan1005.gitprefix.icons.GitPrefixIcons;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Conditions;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.SyntaxTraverser;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;


import static com.github.syuchan1005.gitprefix.grammar.mixin.PrefixResourceBlockExprMixin.BlockExprType;

public class PrefixStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
	private PsiElement element;

	public PrefixStructureViewElement(@NotNull PsiElement element) {
		this.element = element;
	}

	@Override
	public Object getValue() {
		return element;
	}

	@Override
	public void navigate(boolean b) {
		if (element instanceof NavigatablePsiElement) ((NavigatablePsiElement) element).navigate(b);
	}

	@Override
	public boolean canNavigate() {
		if (element instanceof NavigatablePsiElement) {
			return ((NavigatablePsiElement) element).canNavigate();
		}
		return false;
	}

	@Override
	public boolean canNavigateToSource() {
		if (element instanceof NavigatablePsiElement) {
			return ((NavigatablePsiElement) element).canNavigateToSource();
		}
		return false;
	}

	@NotNull
	@Override
	public String getAlphaSortKey() {
		if (element instanceof NavigatablePsiElement) {
			String name = ((NavigatablePsiElement) element).getName();
			return name != null ? name : "none";
		}
		return element.getText();
	}

	@NotNull
	@Override
	public ItemPresentation getPresentation() {
		if (element instanceof PrefixResourceProperty) {
			PrefixResourceProperty property = (PrefixResourceProperty) this.element;
			return new PresentationData(property.getKey(), property.getValueText(), property.getIcon(), null);
		} else if (element instanceof PrefixResourceNamedBlock) {
			PrefixResourceNamedBlock group = (PrefixResourceNamedBlock) this.element;
			return new PresentationData(group.getBlockName().getText(), "", GitPrefixIcons.STRUCTURE.BRACE, null);
		} else if (element instanceof PrefixResourceBlockExpr) {
			BlockExprType exprType = ((PrefixResourceBlockExpr) element).getExprType();
			return new PresentationData(element.getLastChild().getText(), "", exprType.getIcon(), null);
		} else if (element instanceof NavigatablePsiElement) {
			ItemPresentation presentation = ((NavigatablePsiElement) element).getPresentation();
			return presentation != null ? presentation : new PresentationData();
		}
		return new PresentationData();
	}

	@NotNull
	@Override
	public TreeElement[] getChildren() {
		Function<PsiElement, Condition<PsiElement>> structurePsiCondition =
				element -> Conditions.and(
						Conditions.instanceOf(PrefixResourceNamedBlock.class, PrefixResourceProperty.class, PrefixResourceBlockExpr.class),
						e -> e.getParent() == element
				);
		if (element instanceof PrefixResourceFile) {
			return SyntaxTraverser.psiTraverser(element)
					.traverse()
					.filter(structurePsiCondition.apply(element))
					.transform(PrefixStructureViewElement::new)
					.toList().toArray(new TreeElement[0]);
		} else if (element instanceof PrefixResourceNamedBlock) {
			return SyntaxTraverser.psiTraverser(element)
					.traverse()
					.filter(structurePsiCondition.apply(element))
					.transform(PrefixStructureViewElement::new)
					.toList().toArray(new TreeElement[0]);
		}
		return EMPTY_ARRAY;
	}
}
