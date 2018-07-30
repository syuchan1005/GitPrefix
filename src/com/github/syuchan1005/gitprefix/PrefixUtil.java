package com.github.syuchan1005.gitprefix;

import com.intellij.openapi.util.IconLoader;
import com.intellij.util.ResourceUtil;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.Icon;

public class PrefixUtil {
	private static Map<String, Icon> emojiMap = new HashMap<>(890);

	private static Icon loadEmoji(String emoji) {
		return IconLoader.findIcon("/emojis/" + emoji + ".png");
	}

	public static Map<String, Icon> getEmojiMap() {
		return emojiMap;
	}

	public static Icon getIcon(String emoji) {
		if (emojiMap.containsKey(emoji)) return emojiMap.get(emoji);
		Icon icon = loadEmoji(emoji);
		if (icon != null) {
			emojiMap.put(emoji, icon);
			return icon;
		}
		return null;
	}
}
