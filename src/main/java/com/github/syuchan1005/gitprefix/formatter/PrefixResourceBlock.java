package com.github.syuchan1005.gitprefix.formatter;

import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes;
import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.WrapType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrefixResourceBlock extends AbstractBlock {
	private SpacingBuilder spacingBuilder;

	protected PrefixResourceBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment,
								  SpacingBuilder spacingBuilder) {
		super(node, wrap, alignment);
		this.spacingBuilder = spacingBuilder;
	}

	@Override
	protected List<Block> buildChildren() {
		List<Block> blocks = new ArrayList<>();
		ASTNode child = myNode.getFirstChildNode();
		while (child != null) {
			if (child.getElementType() != TokenType.WHITE_SPACE) {
				Block block = new PrefixResourceBlock(child, Wrap.createWrap(WrapType.NONE, false),
						Alignment.createAlignment(), spacingBuilder);
				blocks.add(block);
			}
			child = child.getTreeNext();
		}
		return blocks;
	}

	@Override
	public Indent getIndent() {
		ASTNode treeParent = myNode.getTreeParent();
		if (treeParent != null && treeParent.getElementType() == PrefixResourceTypes.NAMED_BLOCK) {
			if (myNode.getElementType() == PrefixResourceTypes.PROPERTY ||
					myNode.getElementType() == PrefixResourceTypes.NAMED_BLOCK ||
					myNode.getElementType() == PrefixResourceTypes.BLOCK_EXPR)
				return Indent.getNormalIndent();
		}
		return Indent.getNoneIndent();
	}

	@Nullable
	@Override
	public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
		return spacingBuilder.getSpacing(this, child1, child2);
	}

	@Override
	public boolean isLeaf() {
		return myNode.getFirstChildNode() == null;
	}
}
