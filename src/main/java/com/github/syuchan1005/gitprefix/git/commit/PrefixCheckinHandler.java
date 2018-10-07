package com.github.syuchan1005.gitprefix.git.commit;

import com.github.syuchan1005.gitprefix.extension.PrefixPanelFactory;
import com.github.syuchan1005.gitprefix.grammar.PrefixResourceFile;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceProperty;
import com.github.syuchan1005.gitprefix.ui.PrefixButton;
import com.github.syuchan1005.gitprefix.ui.PrefixPanel;
import com.github.syuchan1005.gitprefix.util.PrefixResourceFileUtil;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.LocalChangeList;
import com.intellij.openapi.vcs.changes.ui.EditChangelistSupport;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.ui.CommitMessage;
import com.intellij.tasks.impl.TaskManagerImpl;
import com.intellij.ui.EditorTextField;
import com.intellij.util.Consumer;
import java.awt.Component;
import java.lang.reflect.Field;
import javax.swing.JPanel;
import org.jetbrains.annotations.Nullable;

public class PrefixCheckinHandler extends CheckinHandler implements EditChangelistSupport {
	private static final ExtensionPointName<PrefixPanelFactory> extensionPointName = new ExtensionPointName<>("com.github.syuchan1005.emojiprefix.prefixPanelFactory");

	private PrefixPanel prefixPanel;
	private CheckinProjectPanel checkinProjectPanel;

	public PrefixCheckinHandler(Project project, TaskManagerImpl taskManager) {
	}

	public PrefixCheckinHandler(CheckinProjectPanel checkinProjectPanel) {
		this.checkinProjectPanel = checkinProjectPanel;
		prefixPanel = new PrefixPanel(checkinProjectPanel.getProject());
		if (prefixPanel.notExist()) return;
		try {
			Splitter splitter = (Splitter) checkinProjectPanel.getComponent();
			injectPrefixPanel(splitter);
		} catch (Exception ignored) {
		}
	}

	@Override
	public void installSearch(EditorTextField name, EditorTextField comment) {
		PrefixCheckinHandler handler = PrefixCheckinHandlerFactory.getHandler();
		if (handler.checkinProjectPanel == null) return;
		Splitter splitter = null;
		CheckinProjectPanel dialog = handler.checkinProjectPanel;
		if (dialog.getComponent() instanceof Splitter) {
			splitter = (Splitter) dialog.getComponent();
		} else {
			try {
				Field mySplitter = dialog.getClass().getDeclaredField("mySplitter");
				mySplitter.setAccessible(true);
				splitter = (Splitter) mySplitter.get(dialog);
			} catch (ReflectiveOperationException ignored) {
			}
		}
		if (splitter != null) handler.injectPrefixPanel(splitter);
	}

	private PrefixButton prefixButton;

	private void injectPrefixPanel(Splitter splitter) {
		PrefixResourceFile prefixFile = PrefixResourceFileUtil.getFromSetting(checkinProjectPanel.getProject());
		if (prefixFile == null) return;

		CommitMessage commitMessage = (CommitMessage) splitter.getSecondComponent();
		JPanel panel = new JPanel();
		panel.setLayout(new VerticalFlowLayout(true, true));
		prefixButton = new PrefixButton(checkinProjectPanel.getProject());
		prefixButton.settingPopup(PrefixResourceFileUtil.BlockType.COMMIT);
		panel.add(prefixButton);
		Component commitTextField = commitMessage.getComponent(0);
		panel.add(commitTextField);
		commitMessage.add(panel, 0);

		String comment = commitMessage.getComment();
		String key = PrefixResourceFileUtil.BlockType.COMMIT.containsKey(comment);
		if (key != null) comment = comment.substring(key.length()).trim();
		commitMessage.setCommitMessage(comment);

		for (PrefixPanelFactory factory : extensionPointName.getExtensions()) {
			factory.createPanel(commitMessage);
		}
	}

	@Override
	public ReturnResult beforeCheckin() {
		if (prefixPanel.notExist()) return ReturnResult.COMMIT;
		for (PrefixPanelFactory prefixPanelFactory : extensionPointName.getExtensions()) {
			if (prefixPanelFactory.beforeCheckin() == PrefixPanelFactory.ReturnResult.CANCEL) {
				return ReturnResult.CANCEL;
			}
		}
		PrefixResourceProperty currentProperty = prefixButton.getCurrentProperty();
		if (currentProperty == null) return ReturnResult.COMMIT;
		checkinProjectPanel.setCommitMessage(currentProperty.getKey() + " " + checkinProjectPanel.getCommitMessage());
		return ReturnResult.COMMIT;
	}

	@Override
	public Consumer<LocalChangeList> addControls(JPanel bottomPanel, @Nullable LocalChangeList initial) {
		return null;
	}

	@Override
	public void changelistCreated(LocalChangeList changeList) {
	}
}
