// This is a generated file. Not intended for manual editing.
package com.github.syuchan1005.gitprefix.grammar.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;


import static com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes.*;
import com.github.syuchan1005.gitprefix.grammar.mixin.PrefixResourceBlockExprMixin;
import com.github.syuchan1005.gitprefix.grammar.psi.*;

public class PrefixResourceBlockExprImpl extends PrefixResourceBlockExprMixin implements PrefixResourceBlockExpr {

  public PrefixResourceBlockExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PrefixResourceVisitor visitor) {
    visitor.visitBlockExpr(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PrefixResourceVisitor) accept((PrefixResourceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getBlockName() {
    return findNotNullChildByType(BLOCK_NAME);
  }

}
