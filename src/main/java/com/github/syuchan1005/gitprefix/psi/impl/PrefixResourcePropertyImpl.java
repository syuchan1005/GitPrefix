// This is a generated file. Not intended for manual editing.
package com.github.syuchan1005.gitprefix.psi.impl;

import com.github.syuchan1005.gitprefix.psi.PrefixResourceProperty;
import com.github.syuchan1005.gitprefix.psi.PrefixResourceVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class PrefixResourcePropertyImpl extends ASTWrapperPsiElement implements PrefixResourceProperty {

	public PrefixResourcePropertyImpl(@NotNull ASTNode node) {
		super(node);
	}

	public void accept(@NotNull PrefixResourceVisitor visitor) {
		visitor.visitProperty(this);
	}

	public void accept(@NotNull PsiElementVisitor visitor) {
		if (visitor instanceof PrefixResourceVisitor) accept((PrefixResourceVisitor) visitor);
		else super.accept(visitor);
	}

}
