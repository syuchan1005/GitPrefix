package com.github.syuchan1005.gitprefix.git.commit;

import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import git4idea.checkin.GitCheckinHandlerFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Created by syuchan on 2017/05/28.
 */
public class PrefixCheckinHandlerFactory extends GitCheckinHandlerFactory {
	private static PrefixCheckinHandler handler;

	public static PrefixCheckinHandler getHandler() {
		return handler;
	}

	@NotNull
	@Override
	protected CheckinHandler createVcsHandler(CheckinProjectPanel panel) {
		handler = new PrefixCheckinHandler();
		handler.setCheckinProjectPanel(panel);
		return handler;
	}
}
