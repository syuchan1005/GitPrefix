// This is a generated file. Not intended for manual editing.
package com.github.syuchan1005.gitprefix.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.github.syuchan1005.gitprefix.psi.impl.*;

public interface PrefixResourceTypes {

  IElementType PROPERTY = new PrefixResourceElementType("PROPERTY");

  IElementType BLOCK_COMMENT = new PrefixResourceTokenType("BLOCK_COMMENT");
  IElementType EMOJI_KEY = new PrefixResourceTokenType("EMOJI_KEY");
  IElementType EMOJI_VALUE = new PrefixResourceTokenType("EMOJI_VALUE");
  IElementType LINE_COMMENT = new PrefixResourceTokenType("LINE_COMMENT");
  IElementType TEXT_KEY = new PrefixResourceTokenType("TEXT_KEY");
  IElementType TEXT_VALUE = new PrefixResourceTokenType("TEXT_VALUE");
  IElementType WHITE_SPACE = new PrefixResourceTokenType("WHITE_SPACE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == PROPERTY) {
        return new PrefixResourcePropertyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
