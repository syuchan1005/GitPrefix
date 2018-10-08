package com.github.syuchan1005.gitprefix.grammar.psi;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;


import static com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes.*;
import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;

@SuppressWarnings("ALL")
%%

%{
  public PrefixResourceLexer() { this(null); }
%}


%public
%class PrefixResourceLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode
%ignorecase

// EOL=\R
WHITE_SPACE=\s+

LINE_COMMENT="//".*
// BLOCK_COMMENT="/"\*(.|\n)*\*"/"

BLOCK_NAME=[^\s:|/{}.>][^\s:|/{}]*
LEFT_BRACE="{"
RIGHT_BRACE="}"
EXPAND_BLOCK="..."
INNER_BLOCK=">"

VALUE=[^\s:|/{}][^\n/{}]*
// EMOJI_KEY=:([^:\n]|.)*?:
// TEXT_KEY=\|([^|\n]|.)*?\|

%state BLOCK_CMNT
%state EMOJI TEXT VAL
%%
<YYINITIAL> {
  {LINE_COMMENT} { return LINE_COMMENT; }

  {BLOCK_NAME} { return BLOCK_NAME; }
  {LEFT_BRACE} { return LEFT_BRACE; }
  {RIGHT_BRACE} { return RIGHT_BRACE; }
  {EXPAND_BLOCK} { return EXPAND_BLOCK; }
  {INNER_BLOCK} { return INNER_BLOCK; }

  "/*" { yybegin(BLOCK_CMNT); }
  ":"  { yybegin(EMOJI); }
  "|"  { yybegin(TEXT); }

  {WHITE_SPACE} { return WHITE_SPACE; }

  <EMOJI> {
    [^:\n] {}
    ":" { yybegin(VAL); return EMOJI_KEY; }
  }

  <TEXT> {
    [^|\n] {}
    "|" { yybegin(VAL); return TEXT_KEY; }
  }

  <BLOCK_CMNT> {
    [^]   {}
    "*/" { yybegin(YYINITIAL); return BLOCK_COMMENT; }
  }
}

<VAL> {
  \n 		{ yybegin(YYINITIAL); return WHITE_SPACE; }
  [ \t] 	{ return WHITE_SPACE; }
  {VALUE} { yybegin(YYINITIAL); return VALUE; }
}

[^] { return BAD_CHARACTER; }
