package com.github.syuchan1005.gitprefix.quickfix;

import com.github.syuchan1005.gitprefix.grammar.PrefixResourceElementFactory;
import com.github.syuchan1005.gitprefix.grammar.PrefixResourceFile;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceNamedBlock;
import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.util.IncorrectOperationException;
import java.util.Objects;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class CreateNamedBlockQuickFix extends BaseIntentionAction {
	private String key;

	public CreateNamedBlockQuickFix(String key) {
		this.key = key;
	}

	@NotNull
	@Override
	public String getText() {
		return "Create namedBlock";
	}

	@Nls(capitalization = Nls.Capitalization.Sentence)
	@NotNull
	@Override
	public String getFamilyName() {
		return "GitPrefix namedBlock";
	}

	@Override
	public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
		return true;
	}

	@Override
	public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
		WriteCommandAction.runWriteCommandAction(project, () -> {
			PrefixResourceFile resourceFile = (PrefixResourceFile) file;
			ASTNode lastChildNode = resourceFile.getNode().getLastChildNode();
			if (lastChildNode != null) {
				ASTNode node = PrefixResourceElementFactory.createFile(project, "\n\n").getFirstChild().getNode();
				if (lastChildNode.getElementType() == TokenType.WHITE_SPACE) {
					resourceFile.getNode().replaceChild(lastChildNode, node);
				} else {
					resourceFile.getNode().addChild(node);
				}
			}
			PrefixResourceNamedBlock namedBlock = PrefixResourceElementFactory.createNamedBlock(project, key);
			resourceFile.getNode().addChild(namedBlock.getNode());
			((Navigatable) namedBlock.getLastChild().getNavigationElement()).navigate(true);
			Objects.requireNonNull(FileEditorManager.getInstance(project).getSelectedTextEditor()).getCaretModel()
					.moveCaretRelatively(0, -1, false, false, false);
		});
	}
}
