// This is a generated file. Not intended for manual editing.
package com.github.syuchan1005.gitprefix.grammar.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PrefixResourceNamedBlock extends PsiElement {

  @NotNull
  List<PrefixResourceBlockExpr> getBlockExprList();

  @NotNull
  List<PrefixResourceNamedBlock> getNamedBlockList();

  @NotNull
  List<PrefixResourceProperty> getPropertyList();

  @NotNull
  PsiElement getBlockName();

  @NotNull
  String getName();

  @NotNull
  PsiElement setName(@NotNull String name);

  @NotNull
  PsiElement getNameIdentifier();

}
