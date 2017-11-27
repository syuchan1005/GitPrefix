package com.github.syuchan1005.emojiprefix.highlight;

import com.github.syuchan1005.emojiprefix.psi.EmojiResourceLexerAdapter;
import com.github.syuchan1005.emojiprefix.psi.EmojiResourceTypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;


import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class EmojiResourceSyntaxHighlighter extends SyntaxHighlighterBase {
	public static final TextAttributesKey KEY = createTextAttributesKey("EMOJI_RESOURCE_KEY", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey VALUE = createTextAttributesKey("EMOJI_RESOURCE_VALUE", DefaultLanguageHighlighterColors.STRING);
	public static final TextAttributesKey COMMENT = createTextAttributesKey("EMOJI_RESOURCE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
	public static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("EMOJI_RESOURCE_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

	private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];
	private static Map<IElementType, TextAttributesKey[]> textAttrMap = new HashMap<>();
	static {
		textAttrMap.put(EmojiResourceTypes.KEY, new TextAttributesKey[]{KEY});
		textAttrMap.put(EmojiResourceTypes.VALUE, new TextAttributesKey[]{VALUE});
		textAttrMap.put(EmojiResourceTypes.COMMENT, new TextAttributesKey[]{COMMENT});
		textAttrMap.put(TokenType.BAD_CHARACTER, new TextAttributesKey[]{BAD_CHARACTER});
	}

	@NotNull
	@Override
	public Lexer getHighlightingLexer() {
		return new EmojiResourceLexerAdapter();
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType type) {
		if (textAttrMap.containsKey(type)) {
			return textAttrMap.get(type);
		}
		return EMPTY_KEYS;
	}
}
