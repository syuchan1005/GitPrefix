package com.github.syuchan1005.emojiprefix;

import com.intellij.openapi.util.IconLoader;
import com.intellij.util.ui.UIUtil;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.jetbrains.annotations.NotNull;

public class EmojiUtil {
	private static Map<String, Icon> emojiMap = new HashMap<>(890);

	static {
		try {
			Icon icon = IconLoader.getIcon("/icons/+1.png");
			Field myUrl = icon.getClass().getDeclaredField("myUrl");
			myUrl.setAccessible(true);
			URL url = (URL) myUrl.get(icon);
			File parentFile = new File(url.getFile()).getParentFile();
			if (!parentFile.getParent().endsWith(".jar!")) {
				String[] emojiList = new File(EmojiUtil.class.getResource("/icons").toURI()).list();
				assert emojiList != null;
				for (String emoji : emojiList) {
					try (InputStream stream = EmojiUtil.class.getResourceAsStream("/icons/" + emoji)) {
						emojiMap.put(emoji.substring(0, emoji.length() - 4), createIcon(stream));
					}
				}
			} else {
				String jarPath = parentFile.getParent().replace("file:", "").replace("%20", " ");
				JarFile jarFile = new JarFile(jarPath.substring(0, jarPath.length() - 1));
				for (JarEntry jarEntry : Collections.list(jarFile.entries())) {
					String name = jarEntry.getName();
					if (name.startsWith("icons/") && name.endsWith(".png")) {
						try (InputStream stream = jarFile.getInputStream(jarEntry)) {
							emojiMap.put(name.substring(6, name.length() - 4), createIcon(stream));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Icon createIcon(InputStream stream) throws IOException {
		int size = UIUtil.isRetina() ? 32 : 16;
		return new ImageIcon(resizeImage(ImageIO.read(stream), size, size));
	}

	private static BufferedImage resizeImage(BufferedImage image, int width, int height) {
		BufferedImage thumb = UIUtil.createImage(width, height, BufferedImage.TYPE_INT_ARGB);
		thumb.getGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 0, 0, width, height, null);
		return thumb;
	}

	public static Map<String, Icon> getEmojiMap() {
		return emojiMap;
	}

	public static Icon getIcon(String emoji) {
		return emojiMap.get(emoji);
	}
}