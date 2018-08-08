package com.github.syuchan1005.gitprefix.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import com.intellij.lexer.FlexLexer;

@SuppressWarnings("ALL")
%%

%class PrefixResourceLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%{
	int textState = -1;
%}

/*
EMOJI_SIGN=":"
TEXT_SIGN="|"
LINE_COMMENT_KEY="#"
BLOCK_COMMENT_BEGIN="/*"
BLOCK_COMMENT_END="*/"
*/

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
Spacer = [ \t\f]
WhiteSpace = {LineTerminator} | {Spacer}

TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "#" {InputCharacter}* {LineTerminator}?

StringCharacter = [^\r\n\:\|\\\#]

%state STRING
%%

<YYINITIAL> {
    {EndOfLineComment} { return PrefixResourceTypes.LINE_COMMENT; }
    {TraditionalComment} { return PrefixResourceTypes.BLOCK_COMMENT; }
    {WhiteSpace}+ { return PrefixResourceTypes.WHITE_SPACE; }

	":" { return PrefixResourceTypes.EMOJI_FRAGMENT; }
	"|" { return PrefixResourceTypes.TEXT_FRAGMENT; }

    ":"{StringCharacter}+ {
      	yypushback(yytext().length() - 1);
      	textState = 0;
      	yybegin(STRING);
    }
    "|"{StringCharacter}+ {
      	yypushback(yytext().length() - 1);
      	textState = 1;
      	yybegin(STRING);
    }

    {Spacer}+{StringCharacter}+ {
      	return textState == 0 ? PrefixResourceTypes.EMOJI_VALUE : PrefixResourceTypes.TEXT_VALUE;
    }

    \/[^/]* { return PrefixResourceTypes.BLOCK_COMMENT; }

    {StringCharacter}+ { return PrefixResourceTypes.BAD_CHARACTER; }
}

<STRING> {
	{StringCharacter}*":" {
      	if (textState == 0) {
      		yybegin(YYINITIAL);
        	return PrefixResourceTypes.EMOJI_KEY;
      	}
      	return TokenType.BAD_CHARACTER;
    }

    {StringCharacter}*"|" {
      	if (textState == 1) {
      		yybegin(YYINITIAL);
        	return PrefixResourceTypes.TEXT_KEY;
      	}
      	return TokenType.BAD_CHARACTER;
    }

	{StringCharacter}+ {
      	if (textState == 0) return PrefixResourceTypes.EMOJI_FRAGMENT;
      	if (textState == 1) return PrefixResourceTypes.TEXT_FRAGMENT;
     	return TokenType.CODE_FRAGMENT;
    }

	{LineTerminator} {
      	yybegin(YYINITIAL);
    }
}

