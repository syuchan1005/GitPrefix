package com.github.syuchan1005.gitprefix.icons;

import com.intellij.openapi.util.IconLoader;
import javax.swing.Icon;

public interface GitPrefixIcons {
	Icon FILE_ICON = IconLoader.getIcon("/icons/fileIcon.svg", GitPrefixIcons.class);
	Icon LAYOUT_EDITOR_ONLY = IconLoader.getIcon("/icons/layoutEditorOnly.svg", GitPrefixIcons.class);
	Icon LAYOUT_EDITOR_EMOJILIST = IconLoader.getIcon("/icons/layoutEditorEmojiList.svg", GitPrefixIcons.class);
	Icon LAYOUT_EDITOR_STRUCTURED = IconLoader.getIcon("/icons/layoutEditorStructured.svg", GitPrefixIcons.class);

	interface STRUCTURE {
		Icon BRACE = IconLoader.getIcon("/icons/structureBrace.svg", GitPrefixIcons.class);
		Icon TEXT_PREFIX = IconLoader.getIcon("/icons/structureTextPrefix.svg", GitPrefixIcons.class);

		interface BLOCK {
			Icon EXPAND = IconLoader.getIcon("/icons/structureExpandBlock.svg", GitPrefixIcons.class);
			Icon INNER = IconLoader.getIcon("/icons/structureInnerBlock.svg", GitPrefixIcons.class);
		}
	}
}
