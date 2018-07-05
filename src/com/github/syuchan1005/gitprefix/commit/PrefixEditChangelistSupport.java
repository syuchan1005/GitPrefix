package com.github.syuchan1005.gitprefix.commit;

import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.vcs.changes.LocalChangeList;
import com.intellij.openapi.vcs.changes.ui.CommitChangeListDialog;
import com.intellij.openapi.vcs.changes.ui.EditChangelistSupport;
import com.intellij.ui.EditorTextField;
import com.intellij.util.Consumer;
import java.lang.reflect.Field;
import javax.swing.JPanel;
import org.jetbrains.annotations.Nullable;

public class PrefixEditChangelistSupport implements EditChangelistSupport  {
	@Override
	public void installSearch(EditorTextField editorTextField, EditorTextField editorTextField1) {
		CommitChangeListDialog dialog = (CommitChangeListDialog) PrefixCheckinHandlerFactory.handler.checkinProjectPanel;
		try {
			Field mySplitter = dialog.getClass().getDeclaredField("mySplitter");
			mySplitter.setAccessible(true);
			Splitter splitter = (Splitter) mySplitter.get(dialog);
			PrefixCheckinHandlerFactory.handler.injectPrefixPanel(splitter);
		} catch (ReflectiveOperationException ignored) {}
	}

	@Override
	public Consumer<LocalChangeList> addControls(JPanel jPanel, @Nullable LocalChangeList localChangeList) {
		return null;
	}

	@Override
	public void changelistCreated(LocalChangeList localChangeList) {}
}
