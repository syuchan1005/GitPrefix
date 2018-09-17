// This is a generated file. Not intended for manual editing.
package com.github.syuchan1005.gitprefix.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.github.syuchan1005.gitprefix.psi.impl.*;

public interface PrefixResourceTypes {

  IElementType PROPERTY = new PrefixResourceElementType("PROPERTY");

  IElementType BLOCK_COMMENT = new PrefixResourceTokenType("block_comment");
  IElementType DEP_LINE_COMMENT = new PrefixResourceTokenType("dep_line_comment");
  IElementType EMOJI_KEY = new PrefixResourceTokenType("emoji_key");
  IElementType LINE_COMMENT = new PrefixResourceTokenType("line_comment");
  IElementType TEXT_KEY = new PrefixResourceTokenType("text_key");
  IElementType VALUE = new PrefixResourceTokenType("value");

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
