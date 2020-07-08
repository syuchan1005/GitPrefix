package com.github.syuchan1005.gitprefix.git.commit

import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.changes.CommitContext
import com.intellij.openapi.vcs.checkin.CheckinHandler
import com.intellij.openapi.vcs.checkin.VcsCheckinHandlerFactory
import git4idea.GitVcs
import git4idea.checkin.GitCheckinHandlerFactory

class PrefixCheckinHandlerFactory : VcsCheckinHandlerFactory(GitVcs.getKey()) {
    @Suppress("OverridingDeprecatedMember") // compatible pycharm, appcode etc...
    override fun createVcsHandler(panel: CheckinProjectPanel): CheckinHandler {
        return PrefixCheckinHandler(panel)
    }
}
