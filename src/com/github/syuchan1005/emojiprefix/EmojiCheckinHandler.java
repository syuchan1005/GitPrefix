package com.github.syuchan1005.emojiprefix;

import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.ui.components.JBScrollPane;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Properties;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * Created by syuchan on 2017/05/29.
 */
public class EmojiCheckinHandler extends CheckinHandler {
	private static final String NO_EMOJI = "No Emoji";

	private JPanel emojiPanel;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private CheckinProjectPanel checkinProjectPanel = null;

	public EmojiCheckinHandler(CheckinProjectPanel checkinProjectPanel) {
		emojiPanel = new JPanel();
		emojiPanel.setLayout(new VerticalFlowLayout());
		File resource = new File(checkinProjectPanel.getProject().getBasePath(), ".emojirc");
		if (resource.exists()) {
			try (InputStream stream = new FileInputStream(resource);
				 InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
				Properties properties = new Properties();
				properties.load(reader);
				properties.keySet().forEach(k -> {
					String key = (String) k;
					JRadioButton radioButton = new JRadioButton(":" + key + ": " + properties.getProperty(key));
					buttonGroup.add(radioButton);
					emojiPanel.add(radioButton);
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
			JRadioButton noRadio = new JRadioButton(NO_EMOJI, true);
			buttonGroup.add(noRadio);
			emojiPanel.add(noRadio);
			this.checkinProjectPanel = checkinProjectPanel;
		}
	}

	@Override
	public ReturnResult beforeCheckin() {
		if (checkinProjectPanel == null) return ReturnResult.COMMIT;
		DialogBuilder dialogBuilder = new DialogBuilder(checkinProjectPanel.getProject());
		dialogBuilder.setTitle("Emoji Select");
		int i = dialogBuilder
				.centerPanel(new JBScrollPane(emojiPanel))
				.okActionEnabled(true)
				.show();
		if (i != 0) {
			return ReturnResult.CANCEL;
		} else {
			Collections.list(buttonGroup.getElements()).stream().filter(AbstractButton::isSelected).findFirst().ifPresent(button -> {
				if (!button.getText().equals(NO_EMOJI)) {
					checkinProjectPanel.setCommitMessage(button.getText().split(": ")[0] + ": " + checkinProjectPanel.getCommitMessage());
				}
			});
			return ReturnResult.COMMIT;
		}
	}
}
