package com.github.syuchan1005.emojiprefix.linemark;

import com.github.syuchan1005.emojiprefix.EmojiUtil;
import com.github.syuchan1005.emojiprefix.psi.EmojiResourceProperty;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

public class EmojiIconLineMarkerProvider extends RelatedItemLineMarkerProvider {

	@Override
	protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo> result) {
		if (element instanceof EmojiResourceProperty) {
			PsiElement firstChild = element.getFirstChild();
			String emoji = firstChild.getText().trim().replace(":", "");
			Icon icon = EmojiUtil.getIcon(emoji);
			if (icon != null) {
				NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(icon)
						.setTarget(element);
				result.add(builder.createLineMarkerInfo(element));
			}
		}
	}
}
