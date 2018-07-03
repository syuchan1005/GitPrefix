package com.github.syuchan1005.emojiprefix.commit;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.checkin.BeforeCheckinDialogHandler;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import git4idea.checkin.GitCheckinHandlerFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Created by syuchan on 2017/05/28.
 */
public class EmojiCheckinHandlerFactory extends GitCheckinHandlerFactory {
	public static EmojiCheckinHandler handler;

	@NotNull
	@Override
	protected CheckinHandler createVcsHandler(CheckinProjectPanel panel) {
		handler = new EmojiCheckinHandler(panel);
		return handler;
	}
}
