package com.github.syuchan1005.gitprefix.editor;

import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceNamedBlock;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GitPrefixReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	public GitPrefixReference(@NotNull PsiElement element, TextRange rangeInElement) {
		super(element, rangeInElement);
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode) {
		return Arrays.stream(myElement.getContainingFile().getChildren())
				.filter(element -> element instanceof PrefixResourceNamedBlock)
				// .filter(element -> ((PrefixResourceNamedBlock) element).getBlockName().getText().equals())
				.map(PsiElementResolveResult::new).toArray(ResolveResult[]::new);
	}

	@Nullable
	@Override
	public PsiElement resolve() {
		ResolveResult[] resolveResults = multiResolve(false);
		return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
	}
}
