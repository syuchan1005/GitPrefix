package com.github.syuchan1005.gitprefix;

import com.intellij.openapi.util.IconLoader;
import com.intellij.util.ResourceUtil;
import com.jgoodies.common.base.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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

	static {
		Consumer<String> putEmoji = emoji -> {
			emojiMap.put(emoji, IconLoader.getIcon("/emojis/" + emoji + ".png"));
		};
		URL resource = ResourceUtil.getResource(PrefixUtil.class, "/emojis", "");
		File file = new File(resource.getFile());
		String path = file.getParentFile().getPath();
		if (!path.endsWith(".jar!")) {
			Arrays.stream(Objects.requireNonNull(file.list()))
							.filter(name -> !name.endsWith("@2x.png"))
							.map(name -> name.substring(0, name.length() - 4))
							.forEach(putEmoji);
		} else {
			try {
				new JarFile(path.substring(SystemUtils.IS_OS_WINDOWS ? 6 : 5, path.length() - 1)).stream()
								.filter(entry -> !entry.isDirectory())
								.map(JarEntry::getName)
								.filter(entry -> entry.startsWith("emojis/") && !entry.endsWith("@2x.png"))
								.map(name -> name.substring(7, name.length() - 4))
								.forEach(putEmoji);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Map<String, Icon> getEmojiMap() {
		return emojiMap;
	}

	public static Icon getIcon(String emoji) {
		return emojiMap.get(emoji);
	}
}
