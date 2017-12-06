package com.github.syuchan1005.emojiprefix;

import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.reference.SoftReference;
import com.intellij.spellchecker.FileLoader;
import com.intellij.util.ResourceUtil;
import com.intellij.util.ui.UIUtil;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
			String[] list = new File(EmojiUtil.class.getResource("/icons").toURI()).list();
			assert list != null;
			for (String emoji : list) {
				if (emoji.contains("@2x")) continue;
				emojiMap.put(emoji.substring(0, emoji.length() - 4), IconLoader.getIcon("/icons/" + emoji));
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Icon> getEmojiMap() {
		return emojiMap;
	}

	public static Icon getIcon(String emoji) {
		return emojiMap.get(emoji);
	}
}