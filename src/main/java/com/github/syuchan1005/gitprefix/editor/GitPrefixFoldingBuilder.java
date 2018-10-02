package com.github.syuchan1005.gitprefix.editor;

import com.github.syuchan1005.gitprefix.grammar.PrefixResourceFile;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes;
import com.github.syuchan1005.gitprefix.util.PrefixPsiUtil;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.tree.TokenSet;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import static com.intellij.lang.folding.FoldingDescriptor.EMPTY;

public class GitPrefixFoldingBuilder implements FoldingBuilder {
	@NotNull
	@Override
	public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document) {
		if (!(node.getPsi() instanceof PrefixResourceFile)) return EMPTY;
		ASTNode[] blocks = PrefixPsiUtil.getChildrenRecursive(node, TokenSet.create(PrefixResourceTypes.NAMED_BLOCK))
				.toArray(new ASTNode[0]);
		List<FoldingDescriptor> result = new ArrayList<>();
		for (ASTNode block : blocks) {
			result.add(new FoldingDescriptor(block, block.getTextRange()));
		}
		return result.toArray(new FoldingDescriptor[0]);
	}

	@Nullable
	@Override
	public String getPlaceholderText(@NotNull ASTNode node) {
		int textLength = node.getFirstChildNode().getTextLength();
		return node.getText().substring(0, textLength) + " {...}";
	}

	@Override
	public boolean isCollapsedByDefault(@NotNull ASTNode node) {
		return false;
	}
}
