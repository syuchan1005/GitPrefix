package com.github.syuchan1005.gitprefix.git.commit

import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.changes.CommitContext
import com.intellij.openapi.vcs.checkin.CheckinHandler
import git4idea.checkin.GitCheckinHandlerFactory

class PrefixCheckinHandlerFactory : GitCheckinHandlerFactory() {
    override fun createVcsHandler(panel: CheckinProjectPanel, commitContext: CommitContext): CheckinHandler {
        return PrefixCheckinHandler(panel)
    }
}
