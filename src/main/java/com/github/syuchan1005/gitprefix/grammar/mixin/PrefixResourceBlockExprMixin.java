package com.github.syuchan1005.gitprefix.grammar.mixin;

import com.github.syuchan1005.gitprefix.grammar.PrefixResourceElementFactory;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceBlockExpr;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceNamedBlock;
import com.github.syuchan1005.gitprefix.icons.GitPrefixIcons;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Arrays;

public abstract class PrefixResourceBlockExprMixin extends ASTWrapperPsiElement implements PrefixResourceBlockExpr, PsiNamedElement {
	public PrefixResourceBlockExprMixin(@NotNull ASTNode node) {
		super(node);
	}

	public enum BlockExprType {
		INNER(">", GitPrefixIcons.STRUCTURE.BLOCK.INNER),
		EXPAND("...", GitPrefixIcons.STRUCTURE.BLOCK.EXPAND),
		UNKNOWN("", GitPrefixIcons.STRUCTURE.BRACE);

		private final String str;
		private final Icon icon;

		BlockExprType(String s, Icon i) {
			str = s;
			icon = i;
		}

		public Icon getIcon() {
			return icon;
		}
	}

	@NotNull
	public String getName() {
		return this.getBlockName().getText();
	}

	@NotNull
	public PsiElement setName(@NotNull String name) {
		ASTNode blockNameNode = PrefixResourceElementFactory.createBlockNameNode(this.getProject(), name);
		this.getNode().replaceChild(this.getBlockName().getNode(), blockNameNode);
		return this;
	}

	@NotNull
	public BlockExprType getExprType() {
		for (BlockExprType value : BlockExprType.values()) {
			if (value.str.equals(this.getFirstChild().getText())) return value;
		}
		return BlockExprType.UNKNOWN;
	}

	@Nullable
	public PrefixResourceNamedBlock getTargetNamedBlock() {
		String blockName = this.getBlockName().getText();
		return (PrefixResourceNamedBlock) Arrays.stream(this.getContainingFile().getChildren())
				.filter(e -> e instanceof PrefixResourceNamedBlock)
				.filter(n -> ((PrefixResourceNamedBlock) n).getBlockName().getText().equals(blockName))
				.findFirst().orElse(null);
	}
}
