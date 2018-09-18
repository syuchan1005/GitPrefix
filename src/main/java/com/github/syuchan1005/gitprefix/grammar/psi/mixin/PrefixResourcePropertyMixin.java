package com.github.syuchan1005.gitprefix.grammar.psi.mixin;

import com.github.syuchan1005.gitprefix.EmojiUtil;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceProperty;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.icons.AllIcons;
import com.intellij.lang.ASTNode;
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
			return this.getTextKey().getText();
		}
		return this.getEmojiKey().getText();
	}

	@Nullable
	public EmojiUtil.EmojiData getEmoji() {
		if (this.getEmojiKey() != null) {
			String text = this.getEmojiKey().getText();
			return EmojiUtil.getEmojiData(text.substring(1, text.length() - 1));
		}
		return null;
	}

	@NotNull
	public Icon getIcon() {
		EmojiUtil.EmojiData emoji = getEmoji();
		return emoji != null ? emoji.getIcon() : AllIcons.Nodes.Field;
	}

	@Nullable
	public String getValueText() {
		return (this.getValue() != null) ? this.getValue().getText() : this.getKey();
	}
}
