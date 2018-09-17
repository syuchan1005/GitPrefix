package com.github.syuchan1005.gitprefix.highlight;

import com.github.syuchan1005.gitprefix.psi.PrefixResourceLexerAdapter;
import com.github.syuchan1005.gitprefix.psi.PrefixResourceTypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;


import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class PrefixResourceSyntaxHighlighter extends SyntaxHighlighterBase {
	public static final TextAttributesKey EMOJI_KEY = createTextAttributesKey("EMOJI_RESOURCE_EMOJI_KEY", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey TEXT_KEY = createTextAttributesKey("EMOJI_RESOURCE_TEXT_KEY", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey VALUE = createTextAttributesKey("EMOJI_RESOURCE_TEXT_VALUE", DefaultLanguageHighlighterColors.STRING);
	public static final TextAttributesKey LINE_COMMENT = createTextAttributesKey("EMOJI_RESOURCE_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
	public static final TextAttributesKey BLOCK_COMMENT = createTextAttributesKey("EMOJI_RESOURCE_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);

	private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];
	private static Map<IElementType, TextAttributesKey[]> textAttrMap = new HashMap<>();

	static {
		textAttrMap.put(PrefixResourceTypes.EMOJI_KEY, new TextAttributesKey[]{EMOJI_KEY});
		textAttrMap.put(PrefixResourceTypes.TEXT_KEY, new TextAttributesKey[]{TEXT_KEY});
		textAttrMap.put(PrefixResourceTypes.LINE_COMMENT, new TextAttributesKey[]{LINE_COMMENT});
		textAttrMap.put(PrefixResourceTypes.BLOCK_COMMENT, new TextAttributesKey[]{BLOCK_COMMENT});
		textAttrMap.put(PrefixResourceTypes.VALUE, new TextAttributesKey[]{VALUE});
	}

	@NotNull
	@Override
	public Lexer getHighlightingLexer() {
		return new PrefixResourceLexerAdapter();
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
