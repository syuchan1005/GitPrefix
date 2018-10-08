package com.github.syuchan1005.gitprefix.icons;

import com.intellij.openapi.util.IconLoader;
import javax.swing.Icon;

public interface GitPrefixIcons {
	Icon FILE_ICON = IconLoader.getIcon("/icons/fileIcon.svg");
	Icon FILE_ICON_OLD = IconLoader.getIcon("/icons/fileIcon_old.svg");
	Icon LAYOUT_EDITOR_ONLY = IconLoader.getIcon("/icons/layoutEditorOnly.svg");
	Icon LAYOUT_EDITOR_EMOJILIST = IconLoader.getIcon("/icons/layoutEditorEmojiList.svg");
	Icon LAYOUT_EDITOR_STRUCTURED = IconLoader.getIcon("/icons/layoutEditorStructured.svg");

	interface STRUCTURE {
		Icon BRACE = IconLoader.getIcon("/icons/structureBrace.svg");
		Icon TEXT_PREFIX = IconLoader.getIcon("/icons/structureTextPrefix.svg");

		interface BLOCK {
			Icon EXPAND = IconLoader.getIcon("/icons/structureExpandBlock.svg");
			Icon INNER = IconLoader.getIcon("/icons/structureInnerBlock.svg");
		}
	}
}
