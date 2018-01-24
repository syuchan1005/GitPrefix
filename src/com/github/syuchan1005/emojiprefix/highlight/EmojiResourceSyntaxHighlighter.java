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
	private static final TextAttributesKey KEY = createTextAttributesKey("EMOJI_RESOURCE_KEY", DefaultLanguageHighlighterColors.KEYWORD);
	private static final TextAttributesKey VALUE = createTextAttributesKey("EMOJI_RESOURCE_VALUE", DefaultLanguageHighlighterColors.STRING);
	private static final TextAttributesKey LINE_COMMENT = createTextAttributesKey("EMOJI_RESOURCE_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
	private static final TextAttributesKey BLOCK_COMMENT = createTextAttributesKey("EMOJI_RESOURCE_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
	private static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("EMOJI_RESOURCE_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

	private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];
	private static Map<IElementType, TextAttributesKey[]> textAttrMap = new HashMap<>();
	static {
		textAttrMap.put(EmojiResourceTypes.EMOJI_KEY, new TextAttributesKey[]{KEY});
		textAttrMap.put(EmojiResourceTypes.TEXT_KEY, new TextAttributesKey[]{KEY});
		textAttrMap.put(EmojiResourceTypes.VALUE, new TextAttributesKey[]{VALUE});
		textAttrMap.put(EmojiResourceTypes.LINE_COMMENT, new TextAttributesKey[]{LINE_COMMENT});
		textAttrMap.put(EmojiResourceTypes.BLOCK_COMMENT, new TextAttributesKey[]{BLOCK_COMMENT});
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
