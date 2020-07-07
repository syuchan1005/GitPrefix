package com.github.syuchan1005.gitprefix.git.commit

import com.github.syuchan1005.gitprefix.util.PrefixResourceFileUtil
import com.github.syuchan1005.gitprefix.util.getFlattenPropertyList
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.changes.CommitExecutor
import com.intellij.openapi.vcs.checkin.CheckinHandler
import com.intellij.psi.SmartPointerManager
import com.intellij.ui.EditorTextField
import com.intellij.util.PairConsumer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PrefixCheckinHandler(private val panel: CheckinProjectPanel) : CheckinHandler() {

    init {
        initText()
    }

    private fun initText() {
        val resourceFile = PrefixResourceFileUtil.getFromSetting(panel.project) ?: return
        val propertyList = PrefixResourceFileUtil.BlockType.COMMIT
                .getBlock(PrefixResourceFileUtil.createStructuredFile(resourceFile, false))
                .getFlattenPropertyList()

        GitCommitComboBoxAction.checkinProjectPanel = panel
        GitCommitComboBoxAction.current = null
        val editorTextField = panel.preferredFocusedComponent as? EditorTextField
        editorTextField?.addDocumentListener(object : DocumentListener {

            override fun documentChanged(event: DocumentEvent) {
                val rawText = event.document.text
                for (property in propertyList) {
                    if (rawText.startsWith(property.key)) {
                        ApplicationManager.getApplication().invokeLater {
                            WriteCommandAction.runWriteCommandAction(panel.project) {
                                panel.commitMessage = rawText.substring(property.key.length).trimStart()
                                GitCommitComboBoxAction.current = SmartPointerManager.getInstance(panel.project).createSmartPsiElementPointer(property)
                            }
                        }
                        break
                    }
                }
                editorTextField.removeDocumentListener(this)
            }
        })
    }

    override fun beforeCheckin(executor: CommitExecutor?, additionalDataConsumer: PairConsumer<Any, Any>): ReturnResult {
        if (!panel.commitMessage.startsWith(prefixText)) {
            panel.commitMessage = prefixText + panel.commitMessage
        }
        return ReturnResult.COMMIT
    }

    companion object {
        val prefixText: String
            get() {
                val property = GitCommitComboBoxAction.currentProperty ?: return ""
                return "${property.key.trim()} "
            }
    }
}
