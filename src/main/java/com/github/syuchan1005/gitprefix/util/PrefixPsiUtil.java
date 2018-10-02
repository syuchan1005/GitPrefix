package com.github.syuchan1005.gitprefix.util;

import com.github.syuchan1005.gitprefix.grammar.PrefixResourceFile;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceBlockExpr;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceNamedBlock;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrefixPsiUtil {
	@NotNull
	public static List<ASTNode> getChildrenRecursive(ASTNode root, TokenSet filter) {
		List<ASTNode> result = new ArrayList<>();
		ASTNode[] children = root.getChildren(filter);
		for (ASTNode child : children) {
			result.add(child);
			ASTNode[] children1 = child.getChildren(filter);
			if (children1.length != 0) result.addAll(getChildrenRecursive(child, filter));
		}
		return result;
	}

	@Nullable
	public static PrefixResourceNamedBlock getContainsRootNamedBlock(PsiElement element) {
		PsiElement parent = element.getParent();
		if (parent instanceof PrefixResourceFile) {
			if (element instanceof PrefixResourceNamedBlock) return ((PrefixResourceNamedBlock) element);
			return null;
		} else {
			return getContainsRootNamedBlock(parent);
		}
	}

	@NotNull
	public static List<PrefixResourceBlockExpr> getExprRecursive(@NotNull PrefixResourceNamedBlock block) {
		List<PrefixResourceBlockExpr> result = new ArrayList<>(block.getBlockExprList());
		for (PrefixResourceNamedBlock namedBlock : block.getNamedBlockList()) {
			result.addAll(getExprRecursive(namedBlock));
		}
		return result;
	}

	public static List<PrefixResourceNamedBlock> getTargetBlockRecursive(List<PrefixResourceNamedBlock> targetBlocks,
																		 PrefixResourceNamedBlock targetNamedBlock) {
		List<PrefixResourceNamedBlock> result = new ArrayList<>(targetBlocks);
		if (targetNamedBlock == null) return result;
		List<PrefixResourceBlockExpr> exprRecursive = getExprRecursive(targetNamedBlock);
		for (PrefixResourceBlockExpr blockExpr : exprRecursive) {
			PrefixResourceNamedBlock namedBlock = blockExpr.getTargetNamedBlock();
			if (result.contains(namedBlock)) continue;
			result.add(namedBlock);
			result.addAll(getTargetBlockRecursive(result, namedBlock));
		}
		return result;
	}

	@NotNull
	public static boolean isRecursive(PrefixResourceBlockExpr expr) {
		PrefixResourceNamedBlock containsRootNamedBlock = getContainsRootNamedBlock(expr);
		PrefixResourceNamedBlock targetNamedBlock = expr.getTargetNamedBlock();
		if (containsRootNamedBlock == null || targetNamedBlock == null) return false;
		if (containsRootNamedBlock == targetNamedBlock) return true;

		// nest recursive
		List<PrefixResourceNamedBlock> targetBlocks = getTargetBlockRecursive(Collections.emptyList(), targetNamedBlock);
		return targetBlocks.contains(containsRootNamedBlock);
	}
}
