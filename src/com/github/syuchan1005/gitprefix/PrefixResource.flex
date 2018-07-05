package com.github.syuchan1005.gitprefix.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import com.intellij.lexer.FlexLexer;

%%

%class PrefixResourceLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%{
	int keyState = 0;
%}

EMOJI_SIGN=":"
TEXT_SIGN="|"

LINE_COMMENT_KEY="#"
BLOCK_COMMENT_BEGIN="/*"
BLOCK_COMMENT_END="*/"


%state WAITING_VALUE WAITING_VALUE VALUE

%%
{LINE_COMMENT_KEY}(.*)\n? { yybegin(YYINITIAL); return PrefixResourceTypes.LINE_COMMENT; }
{BLOCK_COMMENT_BEGIN}[^[*/]]*{BLOCK_COMMENT_END} { return PrefixResourceTypes.BLOCK_COMMENT; }

<YYINITIAL> {
	{EMOJI_SIGN}\S+{EMOJI_SIGN} {
      	yybegin(WAITING_VALUE);
      	keyState = 1;
      	return PrefixResourceTypes.EMOJI_KEY;
      }
	{TEXT_SIGN}(\S|[])+{TEXT_SIGN} {
      	yybegin(WAITING_VALUE);
      	keyState = 2;
      	return PrefixResourceTypes.TEXT_KEY;
      }

	\n+ { return TokenType.WHITE_SPACE; }
	\S+ { return TokenType.BAD_CHARACTER; }
}

<WAITING_VALUE> {
	(\t|[ ])+ { yybegin(VALUE); return TokenType.WHITE_SPACE; }
}

<VALUE> {
	[^:#\|\(\/\*\)\n]+ {
      	yybegin(YYINITIAL);
      	return keyState == 1 ? PrefixResourceTypes.EMOJI_VALUE : PrefixResourceTypes.TEXT_VALUE; }
}
