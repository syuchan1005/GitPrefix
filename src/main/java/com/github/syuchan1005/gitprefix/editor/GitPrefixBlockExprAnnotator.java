package com.github.syuchan1005.gitprefix.editor;

import com.github.syuchan1005.gitprefix.grammar.PrefixResourceFile;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceBlockExpr;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceNamedBlock;
import com.github.syuchan1005.gitprefix.quickfix.CreateNamedBlockQuickFix;
import com.github.syuchan1005.gitprefix.util.PrefixPsiUtil;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public class GitPrefixBlockExprAnnotator implements Annotator {
	@Override
	public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
		if (!(element instanceof PrefixResourceBlockExpr)) return;
		String blockName = ((PrefixResourceBlockExpr) element).getBlockName().getText();
		PrefixResourceFile file = (PrefixResourceFile) element.getContainingFile();
		Optional<String> first = Arrays.stream(file.getChildren())
				.filter(ele -> ele instanceof PrefixResourceNamedBlock)
				.map(block -> ((PrefixResourceNamedBlock) block).getBlockName().getText())
				.filter(text -> text.equals(blockName)).findFirst();
		if (first.isPresent()) {
			boolean isRecursive = PrefixPsiUtil.isRecursive(((PrefixResourceBlockExpr) element));
			if (isRecursive) {
				holder.newAnnotation(HighlightSeverity.ERROR, "Cannot recursive block name")
					.range(((PrefixResourceBlockExpr) element).getBlockName())
					.create();
			} else {
				TextRange range = new TextRange(element.getTextRange().getStartOffset(),
						element.getTextRange().getEndOffset() - blockName.length());
				holder.newAnnotation(HighlightSeverity.INFORMATION, "")
						.range(range)
						.textAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT)
						.create();
			}
		} else {
			holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved block name")
					.range(((PrefixResourceBlockExpr) element).getBlockName())
					.newFix(new CreateNamedBlockQuickFix(blockName))
					.registerFix().create();
		}
	}
}
