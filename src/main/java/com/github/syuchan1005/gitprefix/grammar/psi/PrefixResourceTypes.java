// This is a generated file. Not intended for manual editing.
package com.github.syuchan1005.gitprefix.grammar.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.github.syuchan1005.gitprefix.grammar.PrefixResourceElementType;
import com.github.syuchan1005.gitprefix.grammar.PrefixResourceTokenType;
import com.github.syuchan1005.gitprefix.grammar.psi.impl.*;

public interface PrefixResourceTypes {

  IElementType BLOCK_EXPR = new PrefixResourceElementType("BLOCK_EXPR");
  IElementType EMOJI_KEY = new PrefixResourceElementType("EMOJI_KEY");
  IElementType NAMED_BLOCK = new PrefixResourceElementType("NAMED_BLOCK");
  IElementType PROPERTY = new PrefixResourceElementType("PROPERTY");
  IElementType TEXT_KEY = new PrefixResourceElementType("TEXT_KEY");

  IElementType BLOCK_COMMENT = new PrefixResourceTokenType("BLOCK_COMMENT");
  IElementType BLOCK_NAME = new PrefixResourceTokenType("BLOCK_NAME");
  IElementType EMOJI_WRAP = new PrefixResourceTokenType(":");
  IElementType EXPAND_BLOCK = new PrefixResourceTokenType("...");
  IElementType INNER_BLOCK = new PrefixResourceTokenType(">");
  IElementType KEY_TEXT = new PrefixResourceTokenType("KEY_TEXT");
  IElementType LEFT_BRACE = new PrefixResourceTokenType("{");
  IElementType LINE_COMMENT = new PrefixResourceTokenType("LINE_COMMENT");
  IElementType RIGHT_BRACE = new PrefixResourceTokenType("}");
  IElementType TEXT_WRAP = new PrefixResourceTokenType("|");
  IElementType VALUE = new PrefixResourceTokenType("VALUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == BLOCK_EXPR) {
        return new PrefixResourceBlockExprImpl(node);
      }
      else if (type == EMOJI_KEY) {
        return new PrefixResourceEmojiKeyImpl(node);
      }
      else if (type == NAMED_BLOCK) {
        return new PrefixResourceNamedBlockImpl(node);
      }
      else if (type == PROPERTY) {
        return new PrefixResourcePropertyImpl(node);
      }
      else if (type == TEXT_KEY) {
        return new PrefixResourceTextKeyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
