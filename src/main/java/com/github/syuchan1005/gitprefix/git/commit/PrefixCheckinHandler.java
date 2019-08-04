package com.github.syuchan1005.gitprefix.git.commit;

import com.github.syuchan1005.gitprefix.extension.PrefixPanelFactory;
import com.github.syuchan1005.gitprefix.grammar.PrefixResourceFile;
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceProperty;
import com.github.syuchan1005.gitprefix.ui.PrefixButton;
import com.github.syuchan1005.gitprefix.util.PrefixResourceFileUtil;
import com.intellij.ide.util.projectWizard.AbstractNewProjectStep;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitExecutor;
import com.intellij.openapi.vcs.changes.LocalChangeList;
import com.intellij.openapi.vcs.changes.ui.EditChangelistSupport;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.ui.CommitMessage;
import com.intellij.psi.SmartPointerManager;
import com.intellij.ui.EditorTextField;
import com.intellij.util.Consumer;
import com.intellij.util.PairConsumer;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

public class PrefixCheckinHandler extends CheckinHandler implements EditChangelistSupport {
	private static final ExtensionPointName<PrefixPanelFactory> extensionPointName = new ExtensionPointName<>("com.github.syuchan1005.emojiprefix.prefixPanelFactory");

	private CheckinProjectPanel checkinProjectPanel;

	public void setCheckinProjectPanel(CheckinProjectPanel checkinProjectPanel) {
		this.checkinProjectPanel = checkinProjectPanel;
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

	private void injectPrefixPanel(Splitter splitter) {
		PrefixResourceFile prefixFile = PrefixResourceFileUtil.getFromSetting(checkinProjectPanel.getProject());
		if (prefixFile == null) return;

		CommitMessage commitMessage = (CommitMessage) splitter.getSecondComponent();
		JPanel panel = new JPanel();
		panel.setLayout(new VerticalFlowLayout(true, true));
		PrefixButton prefixButton = new PrefixButton(checkinProjectPanel.getProject(), new PrefixButton.TextHolder() {
            @Override
            public String getText() {
                return checkinProjectPanel.getCommitMessage();
            }

            @Override
            public void setText(String text) {
                checkinProjectPanel.setCommitMessage(text);
            }
        });
		prefixButton.settingPopup(PrefixResourceFileUtil.BlockType.COMMIT);
		if (prefixButton.getPopupMenu() == null) return;
		panel.add(prefixButton);
		Component commitTextField = commitMessage.getComponent(0);
		panel.add(commitTextField);
		commitMessage.add(panel, 0);

		String comment = commitMessage.getComment();
		PrefixResourceProperty key = PrefixResourceFileUtil.BlockType.COMMIT.containsKey(PrefixResourceFileUtil.createStructuredFile(prefixFile, false), comment);
		if (key != null) {
			commitMessage.setCommitMessage(comment.substring(key.getKey().length()).trim());
			prefixButton.setCurrent(SmartPointerManager.getInstance(key.getProject()).createSmartPsiElementPointer(key));
		}

		for (PrefixPanelFactory factory : extensionPointName.getExtensions()) {
			factory.createPanel(commitMessage);
		}
	}

	@Override
	public Consumer<LocalChangeList> addControls(JPanel bottomPanel, @Nullable LocalChangeList initial) {
		return null;
	}

	@Override
	public void changelistCreated(LocalChangeList changeList) { }
}
