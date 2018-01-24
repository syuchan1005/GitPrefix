// This is a generated file. Not intended for manual editing.
package com.github.syuchan1005.emojiprefix.psi;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.github.syuchan1005.emojiprefix.psi.EmojiResourceTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class EmojiResourceParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    if (t == PROPERTY) {
      r = property(b, 0);
    }
    else {
      r = parse_root_(t, b, 0);
    }
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return emojiResourceFile(b, l + 1);
  }

  /* ********************************************************** */
  // EMOJI_KEY|TEXT_KEY
  static boolean KEY(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "KEY")) return false;
    if (!nextTokenIs(b, "", EMOJI_KEY, TEXT_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EMOJI_KEY);
    if (!r) r = consumeToken(b, TEXT_KEY);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // item_*
  static boolean emojiResourceFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "emojiResourceFile")) return false;
    int c = current_position_(b);
    while (true) {
      if (!item_(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "emojiResourceFile", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // property|LINE_COMMENT|BLOCK_COMMENT
  static boolean item_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = property(b, l + 1);
    if (!r) r = consumeToken(b, LINE_COMMENT);
    if (!r) r = consumeToken(b, BLOCK_COMMENT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // KEY VALUE?
  public static boolean property(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property")) return false;
    if (!nextTokenIs(b, "<property>", EMOJI_KEY, TEXT_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROPERTY, "<property>");
    r = KEY(b, l + 1);
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

}
