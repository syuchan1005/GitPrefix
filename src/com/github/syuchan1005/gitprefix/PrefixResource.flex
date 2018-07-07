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

    ":" { yybegin(STRING); }
    "|" { yybegin(STRING); }

    {Spacer}{StringCharacter}+ {
      	return textState == 0 ? PrefixResourceTypes.EMOJI_VALUE : PrefixResourceTypes.TEXT_VALUE;
      }
}

<STRING> {
	{StringCharacter}+":" { yybegin(YYINITIAL); textState = 0; return PrefixResourceTypes.EMOJI_KEY; }
    {StringCharacter}+"|" { yybegin(YYINITIAL); textState = 1; return PrefixResourceTypes.TEXT_KEY; }

	{StringCharacter}+ { /* ignored */ }

	{LineTerminator} { yybegin(YYINITIAL); return TokenType.BAD_CHARACTER; }
}

