package com.github.syuchan1005.gitprefix.grammar;

import com.github.syuchan1005.gitprefix.filetype.PrefixResourceFileType;
import com.github.syuchan1005.gitprefix.filetype.PrefixResourceLanguage;
import com.github.syuchan1005.gitprefix.grammar.mixin.PrefixResourceBlockExprMixin;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceBlockExpr;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceNamedBlock;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceProperty;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes;
import com.github.syuchan1005.gitprefix.util.PrefixPsiUtil;
import com.intellij.codeInsight.actions.RearrangeCodeProcessor;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.TokenSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class PrefixResourceFile extends PsiFileBase {
	protected PrefixResourceFile(@NotNull FileViewProvider viewProvider) {
		super(viewProvider, PrefixResourceLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public FileType getFileType() {
		return PrefixResourceFileType.INSTANCE;
	}

	@Override
	public String toString() {
		return "GitResource File";
	}

	private static List<String> reservedBlockNames = Arrays.asList("commit", "merge", "tag");
	private static TokenSet noNeed = TokenSet.create(TokenType.WHITE_SPACE, PrefixResourceTypes.LINE_COMMENT, PrefixResourceTypes.BLOCK_COMMENT);

	public PrefixResourceFile createStructuredFile() {
		return createStructuredFile(false);
	}

	public PrefixResourceFile createStructuredFile(boolean isPreview) {
		PrefixResourceFile file = (PrefixResourceFile) this.copy();

		/* extract property, calcNamedBlock */
		List<PrefixResourceProperty> properties = new ArrayList<>();
		for (PsiElement child : file.getChildren()) {
			if (child instanceof PrefixResourceProperty) {
				properties.add((PrefixResourceProperty) child);
				file.getNode().removeChild(child.getNode());
			} else if (child instanceof PrefixResourceNamedBlock) {
				calcNamedBlock((PrefixResourceNamedBlock) child);
			} else if (noNeed.contains(child.getNode().getElementType())) {
				file.getNode().removeChild(child.getNode());
			}
		}

		/* remove blocks */
		for (PsiElement child : file.getChildren()) {
			if (child instanceof PrefixResourceNamedBlock) {
				PsiElement blockName = ((PrefixResourceNamedBlock) child).getBlockName();
				if (reservedBlockNames.contains(blockName.getText())) {
					child.getNode().replaceChild(blockName.getNode(), PrefixResourceElementFactory.createBlockNameNode(file.getProject(), "$" + blockName.getText()));
					properties.forEach(n -> {
						child.getNode().addChild(n.copy().getNode(), child.getNode().getLastChildNode().getTreePrev());
					});
				} else {
					file.getNode().removeChild(child.getNode());
				}
			}
		}

		if (isPreview) {
			for (PsiElement child : file.getChildren()) {
				addSpaceAfterProperty((PrefixResourceNamedBlock) child);
			}
			file = PrefixResourceElementFactory.createFile(file.getProject(), file.getText());
			new RearrangeCodeProcessor(new ReformatCodeProcessor(file, false)).runWithoutProgress();
		}
		return file;
	}

	private static void addSpaceAfterProperty(PrefixResourceNamedBlock block) {
		for (PsiElement child : block.getChildren()) {
			block.getNode().addChild(PrefixResourceElementFactory.createLF(block.getProject()).getNode(), child.getNode());
			if (child instanceof PrefixResourceNamedBlock) addSpaceAfterProperty((PrefixResourceNamedBlock) child);
		}
	}

	private static void calcNamedBlock(PrefixResourceNamedBlock block) {
		for (ASTNode child : block.getNode().getChildren(noNeed)) {
			block.getNode().removeChild(child);
		}
		for (PsiElement child : block.getChildren()) {
			if (child instanceof PrefixResourceBlockExpr) {
				PrefixResourceBlockExpr expr = (PrefixResourceBlockExpr) child;
				PrefixResourceNamedBlock targetNamedBlock = expr.getTargetNamedBlock();
				if (targetNamedBlock == null || PrefixPsiUtil.isRecursive(expr)) {
					block.getNode().removeChild(expr.getNode());
				} else {
					ASTNode newChild = targetNamedBlock.getNode().copyElement();
					switch (expr.getExprType()) {
						case EXPAND:
							ASTNode[] children = newChild.getChildren(TokenSet.create(
									PrefixResourceTypes.PROPERTY, PrefixResourceTypes.NAMED_BLOCK));
							for (ASTNode node : children) {
								block.getNode().addChild(node, expr.getNode());
							}
							block.getNode().removeChild(expr.getNode());
							break;
						case INNER:
							block.getNode().replaceChild(expr.getNode(), newChild);
							break;
					}
				}
			} else if (child instanceof PrefixResourceNamedBlock) {
				calcNamedBlock((PrefixResourceNamedBlock) child);
			}
		}
	}
}
