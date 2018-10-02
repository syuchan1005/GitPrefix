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
  IElementType NAMED_BLOCK = new PrefixResourceElementType("NAMED_BLOCK");
  IElementType PROPERTY = new PrefixResourceElementType("PROPERTY");

  IElementType BLOCK_COMMENT = new PrefixResourceTokenType("BLOCK_COMMENT");
  IElementType BLOCK_NAME = new PrefixResourceTokenType("BLOCK_NAME");
  IElementType EMOJI_KEY = new PrefixResourceTokenType("EMOJI_KEY");
  IElementType EXPAND_BLOCK = new PrefixResourceTokenType("...");
  IElementType INNER_BLOCK = new PrefixResourceTokenType(">");
  IElementType LEFT_BRACE = new PrefixResourceTokenType("{");
  IElementType LINE_COMMENT = new PrefixResourceTokenType("LINE_COMMENT");
  IElementType RIGHT_BRACE = new PrefixResourceTokenType("}");
  IElementType TEXT_KEY = new PrefixResourceTokenType("TEXT_KEY");
  IElementType VALUE = new PrefixResourceTokenType("VALUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == BLOCK_EXPR) {
        return new PrefixResourceBlockExprImpl(node);
      }
      else if (type == NAMED_BLOCK) {
        return new PrefixResourceNamedBlockImpl(node);
      }
      else if (type == PROPERTY) {
        return new PrefixResourcePropertyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
