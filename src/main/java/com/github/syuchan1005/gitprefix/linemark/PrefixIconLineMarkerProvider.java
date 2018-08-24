package com.github.syuchan1005.gitprefix.linemark;

import com.github.syuchan1005.gitprefix.PrefixUtil;
import com.github.syuchan1005.gitprefix.psi.PrefixResourceTypes;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import java.util.Collection;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

public class PrefixIconLineMarkerProvider extends RelatedItemLineMarkerProvider {

	@Override
	protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo> result) {
		if (element instanceof LeafPsiElement) {
			LeafPsiElement leafPsiElement = (LeafPsiElement) element;
			if (leafPsiElement.getElementType() != PrefixResourceTypes.EMOJI_KEY) return;
			String emoji = leafPsiElement.getText().trim();
			Icon icon = PrefixUtil.getIcon(emoji.substring(1, emoji.length() - 1));
			if (icon != null) {
				NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(icon).setTarget(element);
				result.add(builder.createLineMarkerInfo(element));
			}
		}
	}
}
