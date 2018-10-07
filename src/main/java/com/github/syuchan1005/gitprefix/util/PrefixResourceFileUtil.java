package com.github.syuchan1005.gitprefix.util;

import com.github.syuchan1005.gitprefix.EmojiUtil;
import com.github.syuchan1005.gitprefix.GitPrefixData;
import com.github.syuchan1005.gitprefix.grammar.PrefixResourceElementFactory;
import com.github.syuchan1005.gitprefix.grammar.PrefixResourceFile;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceBlockExpr;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceNamedBlock;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceProperty;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes;
import com.intellij.codeInsight.actions.RearrangeCodeProcessor;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.tree.TokenSet;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JMenu;
import org.jetbrains.annotations.Nullable;

public class PrefixResourceFileUtil {
	private static TokenSet needElements = TokenSet.create(PrefixResourceTypes.PROPERTY, PrefixResourceTypes.NAMED_BLOCK);

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
			// PrefixResourceFile structuredFile = PrefixResourceFileUtil.createStructuredFile(file, false);
			for (PsiElement child : file.getChildren()) {
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
		public JBPopupMenu createPopupMenu(PrefixResourceFile file, Consumer<PrefixResourceProperty> click) {
			if (file == null) return null;
			PrefixResourceNamedBlock block = this.getBlock(createStructuredFile(file, false));
			if (block == null) return null;
			JBPopupMenu popupMenu = new JBPopupMenu();
			if (a(block, popupMenu, click) == 0) return null;
			return popupMenu;
		}

		private static JBMenuItem toMenuItem(PrefixResourceProperty property, Consumer<PrefixResourceProperty> click) {
			EmojiUtil.EmojiData emoji = property.getEmoji();
			JBMenuItem menuItem;
			if (emoji != null) {
				menuItem = new JBMenuItem(property.getValueText(), emoji.getIcon());
			} else {
				menuItem = new JBMenuItem("|" + property.getKey() + "| " + property.getValueText());
			}
			menuItem.addActionListener(e -> click.accept(property));
			return menuItem;
		}

		private static JMenu toJMenu(PrefixResourceNamedBlock block, Consumer<PrefixResourceProperty> click) {
			JMenu menu = new JMenu(block.getName());
			if (a(block, menu, click) == 0) return null;
			return menu;
		}

		private static int a(PrefixResourceNamedBlock block, Container container, Consumer<PrefixResourceProperty> click) {
			int addCount = 0;
			for (PsiElement child : block.getChildren()) {
				if (child instanceof PrefixResourceProperty) {
					addCount++;
					container.add(toMenuItem((PrefixResourceProperty) child, click));
				} else if (child instanceof PrefixResourceNamedBlock) {
					JMenu menu = toJMenu((PrefixResourceNamedBlock) child, click);
					if (menu != null) addCount++;
					container.add(menu);
				}
			}
			return addCount;
		}

		@Nullable
		public static BlockType getFromBeforeName(String name) {
			for (BlockType value : BlockType.values()) {
				if (value.beforeName.equals(name)) return value;
			}
			return null;
		}
	}

	public static PrefixResourceFile createStructuredFile(PrefixResourceFile prefixResourceFile, boolean isPreview) {
		PrefixResourceFile file = (PrefixResourceFile) prefixResourceFile.copy();

		/* extract property, calcNamedBlock */
		List<PrefixResourceProperty> properties = new ArrayList<>();
		for (PsiElement child : file.getChildren()) {
			if (child instanceof PrefixResourceProperty) {
				properties.add((PrefixResourceProperty) child);
				file.getNode().removeChild(child.getNode());
			} else if (child instanceof PrefixResourceNamedBlock) {
				calcNamedBlock((PrefixResourceNamedBlock) child);
			} else if (!needElements.contains(child.getNode().getElementType())) {
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

		for (BlockType value : BlockType.values()) {
			if (value.getBlock(file) == null) {
				PrefixResourceNamedBlock namedBlock = PrefixResourceElementFactory.createNamedBlock(file.getProject(), value.blockName);
				file.getNode().addChild(namedBlock.getNode());
				properties.forEach(n -> {
					namedBlock.getNode().addChild(n.copy().getNode(), namedBlock.getNode().getLastChildNode().getTreePrev());
				});
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
		for (ASTNode child : block.getNode().getChildren(needElements)) {
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
