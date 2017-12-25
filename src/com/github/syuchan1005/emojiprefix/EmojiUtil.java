package com.github.syuchan1005.emojiprefix;

import com.intellij.openapi.util.IconLoader;
import com.intellij.util.ResourceUtil;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import javax.swing.Icon;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class EmojiUtil {
	@NotNull
	private static Map<String, Icon> emojiMap;

	static {
		Consumer<String> putEmoji = emoji -> {
			emojiMap.put(emoji, IconLoader.getIcon("/emojis/" + emoji + ".png"));
		};
		File file = new File(ResourceUtil.getResource(EmojiUtil.class, "/emojis", "").getFile());
		String path = file.getParentFile().getPath();
		if (!path.endsWith(".jar!")) {
			Arrays.stream(Objects.requireNonNull(file.list()))
					.filter(name -> !name.endsWith("@2x.png"))
					.map(name -> name.substring(0, name.length() - 4))
					.forEach(putEmoji);
		} else {
			try {
				new JarFile(path.substring(6, path.length() - 1)).stream()
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

	@Contract(pure = true)
	@NotNull
	public static Map<String, Icon> getEmojiMap() {
		return emojiMap;
	}

	public static Icon getIcon(String emoji) {
		return emojiMap.get(emoji);
	}
}