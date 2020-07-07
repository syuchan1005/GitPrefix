package com.github.syuchan1005.gitprefix.grammar.mixin;

import com.github.syuchan1005.gitprefix.EmojiUtil;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceProperty;
import com.github.syuchan1005.gitprefix.icons.GitPrefixIcons;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import java.util.Objects;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PrefixResourcePropertyMixin extends ASTWrapperPsiElement implements PrefixResourceProperty {
	public PrefixResourcePropertyMixin(@NotNull ASTNode node) {
		super(node);
	}

	@NotNull
	public String getKey() {
		if (this.getTextKey() != null) {
			String text = this.getTextKey().getText();
			return text.substring(1, text.length() - 1);
		}
		return Objects.requireNonNull(Objects.requireNonNull(this.getEmojiKey())).getText();
	}

	@Nullable
	public EmojiUtil.EmojiData getEmoji() {
		if (this.getEmojiKey() != null) {
			String text = this.getEmojiKey().getText();
			return EmojiUtil.getEmojiMap().get(text.substring(1, text.length() - 1));
		}
		return null;
	}

	@NotNull
	public Icon getIcon() {
		EmojiUtil.EmojiData emoji = getEmoji();
		return emoji != null ? emoji.getIcon() : GitPrefixIcons.STRUCTURE.TEXT_PREFIX;
	}

	@Nullable
	public String getValueText() {
		return (this.getValue() != null) ? this.getValue().getText() : this.getKey();
	}
}
