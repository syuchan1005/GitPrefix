// This is a generated file. Not intended for manual editing.
package com.github.syuchan1005.gitprefix.grammar.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.github.syuchan1005.gitprefix.grammar.mixin.PrefixResourceBlockExprMixin.BlockExprType;

public interface PrefixResourceBlockExpr extends PsiElement {

  @NotNull
  PsiElement getBlockName();

  @NotNull
  String getName();

  @NotNull
  PsiElement setName(@NotNull String name);

  @NotNull
  BlockExprType getExprType();

  @Nullable
  PrefixResourceNamedBlock getTargetNamedBlock();

}
