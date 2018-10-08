// This is a generated file. Not intended for manual editing.
package com.github.syuchan1005.gitprefix.grammar.psi;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings("ALL")
public class PrefixResourceParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    if (t == BLOCK_EXPR) {
      r = block_expr(b, 0);
    }
    else if (t == NAMED_BLOCK) {
      r = named_block(b, 0);
    }
    else if (t == PROPERTY) {
      r = property(b, 0);
    }
    else {
      r = parse_root_(t, b, 0);
    }
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return root(b, l + 1);
  }

  /* ********************************************************** */
  // expand_block_expr | inner_block_expr
  public static boolean block_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_expr")) return false;
    if (!nextTokenIs(b, "<block expr>", EXPAND_BLOCK, INNER_BLOCK)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BLOCK_EXPR, "<block expr>");
    r = expand_block_expr(b, l + 1);
    if (!r) r = inner_block_expr(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // EXPAND_BLOCK BLOCK_NAME
  static boolean expand_block_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expand_block_expr")) return false;
    if (!nextTokenIs(b, EXPAND_BLOCK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, EXPAND_BLOCK, BLOCK_NAME);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // INNER_BLOCK BLOCK_NAME
  static boolean inner_block_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inner_block_expr")) return false;
    if (!nextTokenIs(b, INNER_BLOCK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, INNER_BLOCK, BLOCK_NAME);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // EMOJI_KEY | TEXT_KEY
  static boolean key(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "key")) return false;
    if (!nextTokenIs(b, "", EMOJI_KEY, TEXT_KEY)) return false;
    boolean r;
    r = consumeToken(b, EMOJI_KEY);
    if (!r) r = consumeToken(b, TEXT_KEY);
    return r;
  }

  /* ********************************************************** */
  // BLOCK_NAME LEFT_BRACE (property|named_block|block_expr)* RIGHT_BRACE
  public static boolean named_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "named_block")) return false;
    if (!nextTokenIs(b, BLOCK_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, BLOCK_NAME, LEFT_BRACE);
    r = r && named_block_2(b, l + 1);
    r = r && consumeToken(b, RIGHT_BRACE);
    exit_section_(b, m, NAMED_BLOCK, r);
    return r;
  }

  // (property|named_block|block_expr)*
  private static boolean named_block_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "named_block_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!named_block_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "named_block_2", c)) break;
    }
    return true;
  }

  // property|named_block|block_expr
  private static boolean named_block_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "named_block_2_0")) return false;
    boolean r;
    r = property(b, l + 1);
    if (!r) r = named_block(b, l + 1);
    if (!r) r = block_expr(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // key VALUE?
  public static boolean property(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property")) return false;
    if (!nextTokenIs(b, "<property>", EMOJI_KEY, TEXT_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROPERTY, "<property>");
    r = key(b, l + 1);
    r = r && property_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // VALUE?
  private static boolean property_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_1")) return false;
    consumeToken(b, VALUE);
    return true;
  }

  /* ********************************************************** */
  // root_item*
  static boolean root(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root")) return false;
    while (true) {
      int c = current_position_(b);
      if (!root_item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "root", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !<<eof>> (property|named_block)
  static boolean root_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_item")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = root_item_0(b, l + 1);
    p = r; // pin = 1
    r = r && root_item_1(b, l + 1);
    exit_section_(b, l, m, r, p, root_recover_parser_);
    return r || p;
  }

  // !<<eof>>
  private static boolean root_item_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_item_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !eof(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // property|named_block
  private static boolean root_item_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_item_1")) return false;
    boolean r;
    r = property(b, l + 1);
    if (!r) r = named_block(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // !(key|named_block)
  static boolean root_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !root_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // key|named_block
  private static boolean root_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_recover_0")) return false;
    boolean r;
    r = key(b, l + 1);
    if (!r) r = named_block(b, l + 1);
    return r;
  }

  final static Parser root_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return root_recover(b, l + 1);
    }
  };
}
