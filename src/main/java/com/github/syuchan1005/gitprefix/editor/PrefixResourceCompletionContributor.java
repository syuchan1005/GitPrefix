package com.github.syuchan1005.gitprefix.editor;

import com.github.syuchan1005.gitprefix.EmojiUtil;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceNamedBlock;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.util.ProcessingContext;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class PrefixResourceCompletionContributor extends CompletionContributor {
	public PrefixResourceCompletionContributor() {
		extend(CompletionType.BASIC, PlatformPatterns.psiElement(PsiElement.class), new CompletionProvider<CompletionParameters>() {
			@Override
			protected void addCompletions(@NotNull CompletionParameters completionParameters,
										  @NotNull ProcessingContext processingContext,
										  @NotNull CompletionResultSet completionResultSet) {
				PsiElement position = completionParameters.getPosition();
				if (position.getNode().getElementType() == TokenType.BAD_CHARACTER &&
						position.getText().startsWith(":")) {
					InsertHandler<LookupElement> insertHandler = (insertionContext, lookupElement) -> {
						int startOffset = insertionContext.getStartOffset();
						if (startOffset > 0) {
							insertionContext.getDocument().deleteString(startOffset, startOffset);
						}
					};

					EmojiUtil.getEmojiMap().forEach((key, value) -> completionResultSet.addElement(
							LookupElementBuilder.create(key, key + ":")
									.withIcon(value.getIcon()).withInsertHandler(insertHandler)
					));
				} else if (position.getNode().getElementType() == PrefixResourceTypes.BLOCK_NAME) {
					completionResultSet.addAllElements(
							Arrays.stream(position.getContainingFile().getChildren())
									.filter(e -> e instanceof PrefixResourceNamedBlock)
									.map(e -> LookupElementBuilder.create(((PrefixResourceNamedBlock) e).getName()))
									.collect(Collectors.toList())
					);
				}
			}
		});
	}
}
