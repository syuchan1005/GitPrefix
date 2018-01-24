package com.github.syuchan1005.emojiprefix.completion;

import com.github.syuchan1005.emojiprefix.EmojiUtil;
import com.github.syuchan1005.emojiprefix.psi.EmojiResourceElementType;
import com.github.syuchan1005.emojiprefix.psi.EmojiResourceTypes;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import java.util.Map;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

public class EmojiResourceCompletionContributor extends CompletionContributor {
	public EmojiResourceCompletionContributor() {
		extend(CompletionType.BASIC, PlatformPatterns.psiElement(PsiElement.class), new CompletionProvider<CompletionParameters>() {
			@Override
			protected void addCompletions(@NotNull CompletionParameters completionParameters,
										  ProcessingContext processingContext,
										  @NotNull CompletionResultSet completionResultSet) {
				Document document = completionParameters.getEditor().getDocument();
				int lineStart = document.getLineStartOffset(document.getLineNumber(completionParameters.getOffset()));
				String lineText = document.getText(new TextRange(lineStart, completionParameters.getOffset()));
				if (includeCount(lineText) == 1) {
					for (Map.Entry<String, Icon> iconEntry : EmojiUtil.getEmojiMap().entrySet()) {
						completionResultSet.addElement(LookupElementBuilder.create(iconEntry.getKey(), ":" + iconEntry.getKey() + ":")
								.withIcon(iconEntry.getValue())
								.withInsertHandler((insertionContext, lookupElement) -> {
									int startOffset = insertionContext.getStartOffset();
									Document insertDocument = insertionContext.getDocument();
									if (startOffset > 0 && insertDocument.getCharsSequence().charAt(startOffset - 1) == ':') {
										insertDocument.deleteString(startOffset - 1, startOffset);
									}
								}));
					}
				}
			}
		});
	}

	private int includeCount(String str) {
		int count = 0;
		for (char x : str.toCharArray()) {
			if (x == ':') count++;
		}
		return count;
	}
}
