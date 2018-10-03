package com.github.syuchan1005.gitprefix.grammar;

import com.github.syuchan1005.gitprefix.filetype.PrefixResourceFileType;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceNamedBlock;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;

public class PrefixResourceElementFactory {
	public static PrefixResourceFile createFile(Project project, String text) {
		return (PrefixResourceFile) PsiFileFactory.getInstance(project)
				.createFileFromText("dummy.gitprefix", PrefixResourceFileType.INSTANCE, text);
	}

	public static PrefixResourceNamedBlock createNamedBlock(Project project, String blockName) {
		PrefixResourceFile file = createFile(project, blockName + "{\n\n}");
		return (PrefixResourceNamedBlock) file.getFirstChild();
	}

	public static ASTNode createBlockNameNode(Project project, String blockName) {
		PrefixResourceNamedBlock namedBlock = createNamedBlock(project, blockName);
		return namedBlock.getBlockName().getNode();
	}

	public static PsiElement createLF(Project project) {
		PrefixResourceFile file = createFile(project, "\n");
		return file.getFirstChild();
	}
}
