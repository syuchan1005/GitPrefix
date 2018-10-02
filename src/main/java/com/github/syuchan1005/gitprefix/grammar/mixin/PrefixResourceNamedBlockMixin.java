package com.github.syuchan1005.gitprefix.grammar.mixin;

import com.github.syuchan1005.gitprefix.grammar.PrefixResourceElementFactory;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceNamedBlock;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import org.jetbrains.annotations.NotNull;

public abstract class PrefixResourceNamedBlockMixin extends ASTWrapperPsiElement implements PrefixResourceNamedBlock, PsiNameIdentifierOwner {
	public PrefixResourceNamedBlockMixin(@NotNull ASTNode node) {
		super(node);
	}

	@NotNull
	public String getName() {
		return this.getBlockName().getText();
	}

	@NotNull
	public PsiElement setName(@NotNull String name) {
		this.getNode().replaceChild(this.getBlockName().getNode(),
				PrefixResourceElementFactory.createBlockNameNode(this.getProject(), name));
		return this;
	}

	@NotNull
	public PsiElement getNameIdentifier() {
		return this.getBlockName();
	}
}
