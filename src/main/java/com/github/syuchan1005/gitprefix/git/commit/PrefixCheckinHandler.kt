package com.github.syuchan1005.gitprefix.git.commit

import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.VcsException
import com.intellij.openapi.vcs.changes.CommitExecutor
import com.intellij.openapi.vcs.checkin.CheckinHandler
import com.intellij.util.PairConsumer

class PrefixCheckinHandler(private val myPanel: CheckinProjectPanel) : CheckinHandler() {
    companion object {
        val prefixText: String
            get() {
                val property = GitCommitComboBoxAction.currentProperty ?: return ""
                return "${property.key.trim()} "
            }
    }

    override fun beforeCheckin(executor: CommitExecutor?, additionalDataConsumer: PairConsumer<Any, Any>): ReturnResult {
        myPanel.commitMessage = prefixText + myPanel.commitMessage
        return ReturnResult.COMMIT
    }
}
