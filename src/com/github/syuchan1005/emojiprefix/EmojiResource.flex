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

EMOJI_SIGN=":"
TEXT_SIGN="|"

LINE_COMMENT_KEY="#"
BLOCK_COMMENT_BEGIN="/*"
BLOCK_COMMENT_END="*/"


%state WAITING_VALUE VALUE

%%
{LINE_COMMENT_KEY}(.*)\n? { yybegin(YYINITIAL); return EmojiResourceTypes.LINE_COMMENT; }
{BLOCK_COMMENT_BEGIN}[^[*/]]*{BLOCK_COMMENT_END} { return EmojiResourceTypes.BLOCK_COMMENT; }

<YYINITIAL> {
	{EMOJI_SIGN}\S+{EMOJI_SIGN} { yybegin(WAITING_VALUE); return EmojiResourceTypes.EMOJI_KEY; }
	{TEXT_SIGN}(\S|[ ])*{TEXT_SIGN} { yybegin(WAITING_VALUE); return EmojiResourceTypes.TEXT_KEY; }

	\n+ { return TokenType.WHITE_SPACE; }
	\S+ { return TokenType.BAD_CHARACTER; }
}

<WAITING_VALUE> {
	(\t|[ ])* { yybegin(VALUE); return TokenType.WHITE_SPACE; }
}

<VALUE> {
	[^:#\|\(\/\*\)\n]* { yybegin(YYINITIAL); return EmojiResourceTypes.VALUE; }
}