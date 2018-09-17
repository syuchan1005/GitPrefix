package com.github.syuchan1005.gitprefix.completion;

import com.github.syuchan1005.gitprefix.EmojiUtil;
import com.github.syuchan1005.gitprefix.psi.PrefixResourceTypes;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Document;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class PrefixResourceCompletionContributor extends CompletionContributor {
	public PrefixResourceCompletionContributor() {
		extend(CompletionType.BASIC, PlatformPatterns.psiElement(PsiElement.class), new CompletionProvider<CompletionParameters>() {
			@Override
			protected void addCompletions(@NotNull CompletionParameters completionParameters,
										  ProcessingContext processingContext,
										  @NotNull CompletionResultSet completionResultSet) {
				if (completionParameters.getPosition().getNode().getElementType() == TokenType.BAD_CHARACTER &&
					completionParameters.getPosition().getText().startsWith(":")) {
					InsertHandler<LookupElement> insertHandler = (insertionContext, lookupElement) -> {
						int startOffset = insertionContext.getStartOffset();
						Document insertDocument = insertionContext.getDocument();
						if (startOffset > 0) {
							insertDocument.deleteString(startOffset, startOffset);
						}
					};

					EmojiUtil.getEmojiMap().forEach((key, value) -> {
						completionResultSet.addElement(
								LookupElementBuilder.create(key, key + ":")
										.withIcon(value.getIcon()).withInsertHandler(insertHandler)
						);
					});
				}
			}
		});
	}
}
