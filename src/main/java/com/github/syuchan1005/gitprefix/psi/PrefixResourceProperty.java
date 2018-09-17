// This is a generated file. Not intended for manual editing.
package com.github.syuchan1005.gitprefix.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.github.syuchan1005.gitprefix.EmojiUtil.EmojiData;
import javax.swing.Icon;

public interface PrefixResourceProperty extends PsiElement {

  @Nullable
  PsiElement getEmojiKey();

  @Nullable
  PsiElement getTextKey();

  @Nullable
  PsiElement getValue();

  @NotNull
  String getKey();

  @Nullable
  EmojiData getEmoji();

  @NotNull
  Icon getIcon();

  @Nullable
  String getValueText();

}
