// This is a generated file. Not intended for manual editing.
package generated;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class GeneratedParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    parseLight(root_, builder_);
    return builder_.getTreeBuilt();
  }

  public void parseLight(IElementType root_, PsiBuilder builder_) {
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this, null);
    Marker marker_ = enter_section_(builder_, 0, _COLLAPSE_, null);
    result_ = parse_root_(root_, builder_);
    exit_section_(builder_, 0, marker_, root_, result_, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType root_, PsiBuilder builder_) {
    return parse_root_(root_, builder_, 0);
  }

  static boolean parse_root_(IElementType root_, PsiBuilder builder_, int level_) {
    return root(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // EMOJI_WRAP KEY_TEXT? EMOJI_WRAP
  public static boolean EMOJI_KEY(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "EMOJI_KEY")) return false;
    if (!nextTokenIs(builder_, EMOJI_WRAP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, EMOJI_WRAP);
    result_ = result_ && EMOJI_KEY_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, EMOJI_WRAP);
    exit_section_(builder_, marker_, EMOJI_KEY, result_);
    return result_;
  }

  // KEY_TEXT?
  private static boolean EMOJI_KEY_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "EMOJI_KEY_1")) return false;
    consumeToken(builder_, KEY_TEXT);
    return true;
  }

  /* ********************************************************** */
  // TEXT_WRAP  KEY_TEXT? TEXT_WRAP
  public static boolean TEXT_KEY(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "TEXT_KEY")) return false;
    if (!nextTokenIs(builder_, TEXT_WRAP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TEXT_WRAP);
    result_ = result_ && TEXT_KEY_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, TEXT_WRAP);
    exit_section_(builder_, marker_, TEXT_KEY, result_);
    return result_;
  }

  // KEY_TEXT?
  private static boolean TEXT_KEY_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "TEXT_KEY_1")) return false;
    consumeToken(builder_, KEY_TEXT);
    return true;
  }

  /* ********************************************************** */
  // expand_block_expr | inner_block_expr
  public static boolean block_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_expr")) return false;
    if (!nextTokenIs(builder_, "<block expr>", EXPAND_BLOCK, INNER_BLOCK)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BLOCK_EXPR, "<block expr>");
    result_ = expand_block_expr(builder_, level_ + 1);
    if (!result_) result_ = inner_block_expr(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // EXPAND_BLOCK BLOCK_NAME
  static boolean expand_block_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expand_block_expr")) return false;
    if (!nextTokenIs(builder_, EXPAND_BLOCK)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, EXPAND_BLOCK, BLOCK_NAME);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // INNER_BLOCK BLOCK_NAME
  static boolean inner_block_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "inner_block_expr")) return false;
    if (!nextTokenIs(builder_, INNER_BLOCK)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, INNER_BLOCK, BLOCK_NAME);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // EMOJI_KEY | TEXT_KEY
  static boolean key(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "key")) return false;
    if (!nextTokenIs(builder_, "", EMOJI_WRAP, TEXT_WRAP)) return false;
    boolean result_;
    result_ = EMOJI_KEY(builder_, level_ + 1);
    if (!result_) result_ = TEXT_KEY(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // BLOCK_NAME LEFT_BRACE (property|named_block|block_expr)* RIGHT_BRACE
  public static boolean named_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "named_block")) return false;
    if (!nextTokenIs(builder_, BLOCK_NAME)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, BLOCK_NAME, LEFT_BRACE);
    result_ = result_ && named_block_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_BRACE);
    exit_section_(builder_, marker_, NAMED_BLOCK, result_);
    return result_;
  }

  // (property|named_block|block_expr)*
  private static boolean named_block_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "named_block_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!named_block_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "named_block_2", pos_)) break;
    }
    return true;
  }

  // property|named_block|block_expr
  private static boolean named_block_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "named_block_2_0")) return false;
    boolean result_;
    result_ = property(builder_, level_ + 1);
    if (!result_) result_ = named_block(builder_, level_ + 1);
    if (!result_) result_ = block_expr(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // key VALUE?
  public static boolean property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property")) return false;
    if (!nextTokenIs(builder_, "<property>", EMOJI_WRAP, TEXT_WRAP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PROPERTY, "<property>");
    result_ = key(builder_, level_ + 1);
    result_ = result_ && property_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // VALUE?
  private static boolean property_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_1")) return false;
    consumeToken(builder_, VALUE);
    return true;
  }

  /* ********************************************************** */
  // root_item*
  static boolean root(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "root")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!root_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "root", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !<<eof>> (property|named_block)
  static boolean root_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "root_item")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = root_item_0(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && root_item_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, root_recover_parser_);
    return result_ || pinned_;
  }

  // !<<eof>>
  private static boolean root_item_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "root_item_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !eof(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // property|named_block
  private static boolean root_item_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "root_item_1")) return false;
    boolean result_;
    result_ = property(builder_, level_ + 1);
    if (!result_) result_ = named_block(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // !(key|named_block)
  static boolean root_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "root_recover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !root_recover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // key|named_block
  private static boolean root_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "root_recover_0")) return false;
    boolean result_;
    result_ = key(builder_, level_ + 1);
    if (!result_) result_ = named_block(builder_, level_ + 1);
    return result_;
  }

  static final Parser root_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder_, int level_) {
      return root_recover(builder_, level_ + 1);
    }
  };
}
