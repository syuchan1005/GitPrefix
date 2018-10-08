package com.github.syuchan1005.gitprefix.formatter;

import com.github.syuchan1005.gitprefix.filetype.PrefixResourceLanguage;
import com.intellij.formatting.Alignment;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.WrapType;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import static com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes.*;

public class PrefixResourceFormattingModelBuilder implements FormattingModelBuilder {
	@NotNull
	@Override
	public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
		return FormattingModelProvider
				.createFormattingModelForPsiFile(element.getContainingFile(),
						new PrefixResourceBlock(element.getNode(),
								Wrap.createWrap(WrapType.NONE, false),
								Alignment.createAlignment(),
								createSpaceBuilder(settings)),
						settings);
	}

	@NotNull
	private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
		return new SpacingBuilder(settings, PrefixResourceLanguage.INSTANCE)
				.around(INNER_BLOCK).spaces(1) // >name -> > name
				.around(EXPAND_BLOCK).spaces(0) // ... name -> ...name
				.around(BLOCK_NAME).spaces(1).before(LEFT_BRACE).none() // name{ -> name {
				.around(TokenSet.create(EMOJI_KEY, TEXT_KEY)) // :aa:t -> :aa: t (keep now spaces)
					.parentDependentLFSpacing(1, Integer.MAX_VALUE, true, 0)
				.around(NAMED_BLOCK).blankLines(1)
				.around(PROPERTY).lineBreakOrForceSpace(true, false)
				.around(LEFT_BRACE).lineBreakOrForceSpace(true, false);
	}

	@Nullable
	@Override
	public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
		return null;
	}
}
