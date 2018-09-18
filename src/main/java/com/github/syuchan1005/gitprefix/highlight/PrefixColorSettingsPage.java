package com.github.syuchan1005.gitprefix.highlight;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.github.syuchan1005.gitprefix.icons.GitPrefixIcons;
import java.util.Map;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrefixColorSettingsPage implements ColorSettingsPage {
	private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
			new AttributesDescriptor("EmojiKey", PrefixResourceSyntaxHighlighter.EMOJI_KEY),
			new AttributesDescriptor("TextKey", PrefixResourceSyntaxHighlighter.TEXT_KEY),
			new AttributesDescriptor("Value", PrefixResourceSyntaxHighlighter.VALUE),
			new AttributesDescriptor("LineComment", PrefixResourceSyntaxHighlighter.LINE_COMMENT),
			new AttributesDescriptor("BlockComment", PrefixResourceSyntaxHighlighter.BLOCK_COMMENT),
	};

	@Nullable
	@Override
	public Icon getIcon() {
		return GitPrefixIcons.FILE_ICON;
	}

	@NotNull
	@Override
	public SyntaxHighlighter getHighlighter() {
		return new PrefixResourceSyntaxHighlighter();
	}

	@NotNull
	@Override
	public String getDemoText() {
		return ":tada: Release!!\n\n" +
				"|[Update]| Update Docs\n\n" +
				"// Line Comment\n\n" +
				"/*\n" +
				"Block Comment\n" +
				"*/";
	}

	@Nullable
	@Override
	public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
		return null;
	}

	@NotNull
	@Override
	public AttributesDescriptor[] getAttributeDescriptors() {
		return DESCRIPTORS;
	}

	@NotNull
	@Override
	public ColorDescriptor[] getColorDescriptors() {
		return ColorDescriptor.EMPTY_ARRAY;
	}

	@NotNull
	@Override
	public String getDisplayName() {
		return "Git Prefix";
	}
}
