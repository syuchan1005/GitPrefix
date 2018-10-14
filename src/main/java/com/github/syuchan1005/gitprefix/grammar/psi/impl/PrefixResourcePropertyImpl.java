// This is a generated file. Not intended for manual editing.
package com.github.syuchan1005.gitprefix.grammar.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes.*;
import com.github.syuchan1005.gitprefix.grammar.mixin.PrefixResourcePropertyMixin;
import com.github.syuchan1005.gitprefix.grammar.psi.*;
import com.github.syuchan1005.gitprefix.EmojiUtil.EmojiData;
import javax.swing.Icon;

public class PrefixResourcePropertyImpl extends PrefixResourcePropertyMixin implements PrefixResourceProperty {

  public PrefixResourcePropertyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PrefixResourceVisitor visitor) {
    visitor.visitProperty(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PrefixResourceVisitor) accept((PrefixResourceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PrefixResourceEmojiKey getEmojiKey() {
    return findChildByClass(PrefixResourceEmojiKey.class);
  }

  @Override
  @Nullable
  public PrefixResourceTextKey getTextKey() {
    return findChildByClass(PrefixResourceTextKey.class);
  }

  @Override
  @Nullable
  public PsiElement getValue() {
    return findChildByType(VALUE);
  }

}
