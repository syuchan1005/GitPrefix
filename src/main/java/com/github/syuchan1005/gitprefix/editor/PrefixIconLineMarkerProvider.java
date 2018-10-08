package com.github.syuchan1005.gitprefix.editor;

import com.github.syuchan1005.gitprefix.EmojiUtil;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public class PrefixIconLineMarkerProvider extends RelatedItemLineMarkerProvider {

	@Override
	protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo> result) {
		if (element instanceof LeafPsiElement) {
			LeafPsiElement leafPsiElement = (LeafPsiElement) element;
			RelatedItemLineMarkerInfo<PsiElement> lineMarkerInfo = null;
			if (leafPsiElement.getElementType() == PrefixResourceTypes.EMOJI_KEY) {
				String emoji = leafPsiElement.getText().trim();
				if (emoji.length() < 1) return;
				EmojiUtil.EmojiData emojiData = EmojiUtil.getEmojiData(emoji.substring(1, emoji.length() - 1));
				if (emojiData != null) {
					NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(emojiData.getIcon()).setTarget(element);
					lineMarkerInfo = builder.createLineMarkerInfo(element);
				}
			}
			if (lineMarkerInfo != null) result.add(lineMarkerInfo);
		}
	}
}
