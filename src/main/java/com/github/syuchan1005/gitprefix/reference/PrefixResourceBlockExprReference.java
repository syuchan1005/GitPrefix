package com.github.syuchan1005.gitprefix.reference;

import com.github.syuchan1005.gitprefix.grammar.PrefixResourceElementFactory;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceBlockExpr;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrefixResourceBlockExprReference extends PsiReferenceBase<PrefixResourceBlockExpr> {
	public PrefixResourceBlockExprReference(@NotNull PrefixResourceBlockExpr element) {
		super(element);
	}

	@Nullable
	@Override
	public PsiElement resolve() {
		return myElement.getTargetNamedBlock();
	}

	@Override
	protected TextRange calculateDefaultRangeInElement() {
		return myElement.getBlockName().getTextRangeInParent();
	}

	@Override
	public PsiElement handleElementRename(@NotNull String newElementName) {
		myElement.getNode().replaceChild(myElement.getBlockName().getNode(),
				PrefixResourceElementFactory.createBlockNameNode(myElement.getProject(), newElementName));
		return myElement;
	}
}
