// This is a generated file. Not intended for manual editing.
package com.github.syuchan1005.emojiprefix.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.github.syuchan1005.emojiprefix.psi.EmojiResourceTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.github.syuchan1005.emojiprefix.psi.*;

public class EmojiResourcePropertyImpl extends ASTWrapperPsiElement implements EmojiResourceProperty {

  public EmojiResourcePropertyImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull EmojiResourceVisitor visitor) {
    visitor.visitProperty(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof EmojiResourceVisitor) accept((EmojiResourceVisitor)visitor);
    else super.accept(visitor);
  }

}
