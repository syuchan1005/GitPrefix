package com.github.syuchan1005.emojiprefix;

import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Created by syuchan on 2017/05/28.
 */
public class EmojiCheckinHandlerFactory extends CheckinHandlerFactory {

	@NotNull
	@Override
	public CheckinHandler createHandler(@NotNull CheckinProjectPanel checkinProjectPanel, @NotNull CommitContext commitContext) {
		return new EmojiCheckinHandler(checkinProjectPanel);
	}
}