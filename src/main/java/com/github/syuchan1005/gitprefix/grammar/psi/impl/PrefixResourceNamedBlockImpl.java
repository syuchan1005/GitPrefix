// This is a generated file. Not intended for manual editing.
package com.github.syuchan1005.gitprefix.grammar.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes.*;
import com.github.syuchan1005.gitprefix.grammar.mixin.PrefixResourceNamedBlockMixin;
import com.github.syuchan1005.gitprefix.grammar.psi.*;

public class PrefixResourceNamedBlockImpl extends PrefixResourceNamedBlockMixin implements PrefixResourceNamedBlock {

  public PrefixResourceNamedBlockImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PrefixResourceVisitor visitor) {
    visitor.visitNamedBlock(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PrefixResourceVisitor) accept((PrefixResourceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PrefixResourceBlockExpr> getBlockExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PrefixResourceBlockExpr.class);
  }

  @Override
  @NotNull
  public List<PrefixResourceNamedBlock> getNamedBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PrefixResourceNamedBlock.class);
  }

  @Override
  @NotNull
  public List<PrefixResourceProperty> getPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PrefixResourceProperty.class);
  }

  @Override
  @NotNull
  public PsiElement getBlockName() {
    return findNotNullChildByType(BLOCK_NAME);
  }

}
