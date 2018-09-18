package com.github.syuchan1005.gitprefix.grammar.psi;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes.*;

%%

%{
  public PrefixResourceLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class PrefixResourceLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

// EOL=\R
WHITE_SPACE=\s+

LINE_COMMENT="//".*
DEP_LINE_COMMENT=#.*
// BLOCK_COMMENT="/"\*(.|\n)*\*"/"

VALUE=[^\s:|#/][^\n#/]*
// EMOJI_KEY=:([^:\n]|.)*?:
// TEXT_KEY=\|([^|\n]|.)*?\|

%state BLOCK
%state EMOJI TEXT
%%
<YYINITIAL> {
  {LINE_COMMENT} { return LINE_COMMENT; }
  {DEP_LINE_COMMENT} { return LINE_COMMENT; }
  {VALUE} { return VALUE; }

  "/*" { yybegin(BLOCK); }
  ":" { yybegin(EMOJI); }
  "|" { yybegin(TEXT); }

  {WHITE_SPACE}           { return WHITE_SPACE; }

  <EMOJI> {
  	[^:\n] {}
    ":" { yybegin(YYINITIAL); return EMOJI_KEY; }
  }

  <TEXT> {
    [^|\n] {}
    "|" { yybegin(YYINITIAL); return TEXT_KEY; }
  }

  <BLOCK> {
    [^]   {}
    "*/" { yybegin(YYINITIAL); return BLOCK_COMMENT; }
  }
}

[^] { return BAD_CHARACTER; }
