package com.github.syuchan1005.gitprefix.grammar;

import com.github.syuchan1005.gitprefix.EmojiUtil;
import com.github.syuchan1005.gitprefix.GitPrefixData;
import com.github.syuchan1005.gitprefix.filetype.PrefixResourceFileType;
import com.github.syuchan1005.gitprefix.filetype.PrefixResourceLanguage;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceBlockExpr;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceNamedBlock;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceProperty;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes;
import com.github.syuchan1005.gitprefix.util.PrefixPsiUtil;
import com.intellij.codeInsight.actions.RearrangeCodeProcessor;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.actionSystem.impl.ActionPopupMenuImpl;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.ui.components.JBMenu;
import java.awt.Container;
import java.awt.PopupMenu;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.SystemIndependent;

public class PrefixResourceFile extends PsiFileBase {
	private static TokenSet noNeed = TokenSet.create(TokenType.WHITE_SPACE, PrefixResourceTypes.LINE_COMMENT, PrefixResourceTypes.BLOCK_COMMENT);

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

	public enum BlockType {
		COMMIT("commit", "$commit"),
		MERGE("merge", "$merge"),
		TAG("tag", "$tag");

		private final String beforeName;
		private final String blockName;

		BlockType(String beforeName, String blockName) {
			this.beforeName = beforeName;
			this.blockName = blockName;
		}

		public String getBeforeName() {
			return beforeName;
		}

		public String getBlockName() {
			return blockName;
		}

		@Nullable
		public PrefixResourceNamedBlock getBlock(PrefixResourceFile file) {
			PrefixResourceFile structuredFile = file.createStructuredFile(false);
			for (PsiElement child : structuredFile.getChildren()) {
				if (child instanceof PrefixResourceNamedBlock &&
						this.blockName.equals(((PrefixResourceNamedBlock) child).getName())) {
					return (PrefixResourceNamedBlock) child;
				}
			}
			return null;
		}

		@Nullable
		public String containsKey(String key) {
			return null;
		}

		@Nullable
		public JBPopupMenu createPopupMenu(@NotNull PrefixResourceFile file) {
			PrefixResourceNamedBlock block = this.getBlock(file);
			if (block == null) return null;
			JBPopupMenu popupMenu = new JBPopupMenu();
			a(block, popupMenu);
			return popupMenu;
		}

		private static JBMenuItem toMenuItem(PrefixResourceProperty property) {
			EmojiUtil.EmojiData emoji = property.getEmoji();
			if (emoji != null) {
				return new JBMenuItem(property.getValueText(), emoji.getIcon());
			}
			return new JBMenuItem(property.getKey() + " " + property.getValueText());
		}

		private static JMenu toJMenu(PrefixResourceNamedBlock block) {
			JMenu menu = new JMenu(block.getName());
			a(block, menu);
			return menu;
		}

		private static void a(PrefixResourceNamedBlock block, Container container) {
			for (PsiElement child : block.getChildren()) {
				if (child instanceof PrefixResourceProperty) {
					container.add(toMenuItem((PrefixResourceProperty) child));
				} else if (child instanceof PrefixResourceNamedBlock) {
					container.add(toJMenu((PrefixResourceNamedBlock) child));
				}
			}
		}

		@Nullable
		public static BlockType getFromBeforeName(String name) {
			for (BlockType value : BlockType.values()) {
				if (value.beforeName.equals(name)) return value;
			}
			return null;
		}
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
				BlockType fromBeforeName = BlockType.getFromBeforeName(blockName.getText());
				if (fromBeforeName != null) {
					child.getNode().replaceChild(blockName.getNode(),
							PrefixResourceElementFactory.createBlockNameNode(file.getProject(), fromBeforeName.getBlockName()));
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

	@Nullable
	public static PrefixResourceFile getFromSetting(Project project) {
		GitPrefixData prefixData = GitPrefixData.convertClassLoader(ServiceManager.getService(project, GitPrefixData.class));
		VirtualFile virtualFile;
		if (prefixData.isPathType.equals("DEFAULT")) {
			String basePath = project.getBasePath();
			if (basePath == null) return null;
			VirtualFile baseDir = LocalFileSystem.getInstance().refreshAndFindFileByPath(basePath);
			if (baseDir == null) return null;
			virtualFile = baseDir.findChild(".gitprefix");
		} else if (prefixData.isPathType.equals("CUSTOM") && !prefixData.gitPrefixPath.equals("")) {
			virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(prefixData.gitPrefixPath);
		} else {
			return null;
		}
		if (virtualFile == null) return null;
		PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
		if (!(psiFile instanceof PrefixResourceFile)) return null;
		return (PrefixResourceFile) psiFile;
	}
}
