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


import static com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes.BLOCK_NAME;
import static com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes.EMOJI_KEY;
import static com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes.EXPAND_BLOCK;
import static com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes.INNER_BLOCK;
import static com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes.LEFT_BRACE;
import static com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes.NAMED_BLOCK;
import static com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes.PROPERTY;
import static com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes.TEXT_KEY;

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
				.around(TokenSet.create(EMOJI_KEY, TEXT_KEY)).spaces(1) // :aa:t -> :aa: t
				.around(NAMED_BLOCK).blankLines(1) // block {\n}block {\n} -> block {\n}\n\nblock {\n}
				.around(PROPERTY).lineBreakOrForceSpace(true, false)
				.around(LEFT_BRACE).lineBreakInCode();
	}

	@Nullable
	@Override
	public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
		return null;
	}
}
