// This is a generated file. Not intended for manual editing.
package com.github.syuchan1005.emojiprefix.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.github.syuchan1005.emojiprefix.psi.impl.*;

public interface EmojiResourceTypes {

  IElementType PROPERTY = new EmojiResourceElementType("PROPERTY");

  IElementType BLOCK_COMMENT = new EmojiResourceTokenType("BLOCK_COMMENT");
  IElementType KEY = new EmojiResourceTokenType("KEY");
  IElementType LINE_COMMENT = new EmojiResourceTokenType("LINE_COMMENT");
  IElementType VALUE = new EmojiResourceTokenType("VALUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == PROPERTY) {
        return new EmojiResourcePropertyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
