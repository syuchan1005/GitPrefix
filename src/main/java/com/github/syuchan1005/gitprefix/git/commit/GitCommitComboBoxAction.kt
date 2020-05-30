package com.github.syuchan1005.gitprefix.git.commit

import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceProperty
import com.github.syuchan1005.gitprefix.util.PrefixResourceFileUtil
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.actionSystem.ex.ComboBoxAction
import com.intellij.psi.SmartPointerManager
import com.intellij.psi.SmartPsiElementPointer
import java.awt.Dimension
import javax.swing.JComponent

class GitCommitComboBoxAction : ComboBoxAction() {
    fun setCurrent(property: SmartPsiElementPointer<PrefixResourceProperty>?) {
        current = property
    }

    override fun update(e: AnActionEvent) {
        if (current == null) {
            e.presentation.icon = null
            e.presentation.text = "NO PREFIX"
        } else {
            val p = current!!.element ?: return
            val emoji = p.emoji
            if (emoji != null) {
                e.presentation.icon = emoji.icon
                e.presentation.text = p.valueText
            } else {
                e.presentation.icon = null
                e.presentation.text = "|${p.key}| ${p.valueText}"
            }
        }
    }

    override fun createPopupActionGroup(button: JComponent): DefaultActionGroup {
        // INFO: Not use this
        return DefaultActionGroup()
    }

    override fun createPopupActionGroup(button: JComponent, dataContext: DataContext): DefaultActionGroup {
        val project = dataContext.getData(PlatformDataKeys.PROJECT)
        val group: DefaultActionGroup
        group = if (project != null) {
            val resourceFile = PrefixResourceFileUtil.getFromSetting(project)
            PrefixResourceFileUtil.BlockType.COMMIT.createActionGroup(resourceFile) { property: PrefixResourceProperty ->
                setCurrent(SmartPointerManager.getInstance(project).createSmartPsiElementPointer(property))
            }
        } else {
            DefaultActionGroup()
        }
        group.add(object : AnAction("NO PREFIX") {
            override fun actionPerformed(e: AnActionEvent) {
                setCurrent(null)
            }
        })
        return group
    }

    override fun createCustomComponent(presentation: Presentation, place: String): JComponent {
        presentation.text = "NO PREFIX"
        val boxButton = ComboBoxButton(presentation)
        boxButton.preferredSize = Dimension(65, boxButton.height)

        return boxButton
    }

    companion object {
        private var current: SmartPsiElementPointer<PrefixResourceProperty>? = null

        val currentProperty: PrefixResourceProperty?
            get() = if (current == null) null else current!!.element
    }
}
