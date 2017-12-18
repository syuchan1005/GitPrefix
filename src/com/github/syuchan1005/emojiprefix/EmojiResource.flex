package com.github.syuchan1005.emojiprefix.psi;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.github.syuchan1005.emojiprefix.psi.EmojiResourceTypes;
import com.intellij.psi.TokenType;

%%

%class EmojiResourceLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

NewLine=[\n|\r\n]
LINE_COMMENT_KEY="#"
BLOCK_COMMENT_BEGIN="/*"
BLOCK_COMMENT_END="*/"
WHITE_SPACE=\s
CHARACTER=\S

%state WAITING_VALUE VALUE

%%
{LINE_COMMENT_KEY}(.*){NewLine}? { yybegin(YYINITIAL); return EmojiResourceTypes.LINE_COMMENT; }
{BLOCK_COMMENT_BEGIN}[^[*/]]*{BLOCK_COMMENT_END} { return EmojiResourceTypes.BLOCK_COMMENT; }

<YYINITIAL> {
	:{CHARACTER}+: { yybegin(WAITING_VALUE); return EmojiResourceTypes.KEY; }
	{WHITE_SPACE}* { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }
	{CHARACTER}* { yybegin(YYINITIAL); return TokenType.BAD_CHARACTER; }
}

<WAITING_VALUE> {WHITE_SPACE}* { yybegin(VALUE); return TokenType.WHITE_SPACE; }

<VALUE> {
	{NewLine} { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }
	[^:#\(\/\*\)\n]* { yybegin(YYINITIAL); return EmojiResourceTypes.VALUE; }
}
