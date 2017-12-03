package com.github.syuchan1005.emojiprefix;

import com.intellij.openapi.util.IconLoader;
import com.intellij.util.ui.UIUtil;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmojiUtil {
	private static Map<String, Icon> iconCache = new HashMap<>();
	private static List<String> emojiAliases;
	private static boolean isJar = false;
	private static JarFile jarFile = null;

	static {
		try {
			Icon icon = IconLoader.getIcon("/icons/+1.png");
			Field myUrl = icon.getClass().getDeclaredField("myUrl");
			myUrl.setAccessible(true);
			URL url = (URL) myUrl.get(icon);
			File parentFile = new File(url.getFile()).getParentFile();
			isJar = parentFile.getParent().endsWith(".jar!");
			if (!isJar) {
				String[] emojiList = new File(EmojiUtil.class.getResource("/icons").toURI()).list();
				assert emojiList != null;
				emojiAliases = Arrays.stream(emojiList)
						.map(v -> v.substring(0, v.length() - 4))
						.collect(Collectors.toList());
			} else {
				String jarPath = parentFile.getParent().replace("file:", "").replace("%20", " ");
				jarFile = new JarFile(jarPath.substring(0, jarPath.length() - 1));
				emojiAliases = new ArrayList<>(890);
				for (JarEntry jarEntry : Collections.list(jarFile.entries())) {
					String name = jarEntry.getName();
					if (name.startsWith("icons/") && name.endsWith(".png")) {
						emojiAliases.add(name.substring(6, name.length() - 4));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Nullable
	public static Icon getIcon(String emojiName) {
		if (getEmojiAliases().contains(emojiName)) {
			if (iconCache.containsKey(emojiName)) {
				return iconCache.get(emojiName);
			} else {
				try (InputStream iconStream = isJar ?
						jarFile.getInputStream(jarFile.getJarEntry("icons/" + emojiName + ".png")) :
						EmojiUtil.class.getResourceAsStream("/icons/" + emojiName + ".png")) {
					BufferedImage iconImage = ImageIO.read(iconStream);
					int size = UIUtil.isRetina() ? 32 : 16;
					Icon icon = new ImageIcon(resizeImage(iconImage, size, size));
					iconCache.put(emojiName, icon);
					return icon;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@NotNull
	public static List<String> getEmojiAliases() {
		return emojiAliases;
	}

	private static BufferedImage resizeImage(BufferedImage image, int width, int height) {
		BufferedImage thumb = UIUtil.createImage(width, height, BufferedImage.TYPE_INT_ARGB);
		thumb.getGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 0, 0, width, height, null);
		return thumb;
	}
}