package com.github.syuchan1005.gitprefix.editor;

import com.github.syuchan1005.gitprefix.grammar.PrefixResourceLexerAdapter;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceNamedBlock;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes;
import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GitPrefixFindUsagesProvider implements FindUsagesProvider {
	@Nullable
	@Override
	public WordsScanner getWordsScanner() {
		return new DefaultWordsScanner(new PrefixResourceLexerAdapter(),
				TokenSet.create(PrefixResourceTypes.NAMED_BLOCK),
				TokenSet.create(PrefixResourceTypes.LINE_COMMENT, PrefixResourceTypes.BLOCK_COMMENT),
				TokenSet.EMPTY);
	}

	@Override
	public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
		return psiElement instanceof PsiNamedElement;
	}

	@Nullable
	@Override
	public String getHelpId(@NotNull PsiElement psiElement) {
		return null;
	}

	@NotNull
	@Override
	public String getType(@NotNull PsiElement element) {
		if (element instanceof PrefixResourceNamedBlock) {
			return "GitPrefix NamedBlock";
		}
		return "";
	}

	@NotNull
	@Override
	public String getDescriptiveName(@NotNull PsiElement element) {
		if (element instanceof PrefixResourceNamedBlock) {
			return ((PrefixResourceNamedBlock) element).getName();
		}
		return "";
	}

	@NotNull
	@Override
	public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
		return getDescriptiveName(element);
	}
}
