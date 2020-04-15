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
    private lateinit var boxButton: ComboBoxButton

    fun setCurrent(property: SmartPsiElementPointer<PrefixResourceProperty>?) {
        current = property
        if (current == null) {
            boxButton.icon = null
            boxButton.text = "NO PREFIX"
        } else {
            val p = current!!.element ?: return
            val emoji = p.emoji
            if (emoji != null) {
                boxButton.icon = emoji.icon
                boxButton.text = p.valueText
            } else {
                boxButton.icon = null
                boxButton.text = "|${p.key}| ${p.valueText}"
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
        boxButton = ComboBoxButton(presentation)
        boxButton.preferredSize = Dimension(65, boxButton.height)

        return boxButton
    }

    companion object {
        private var current: SmartPsiElementPointer<PrefixResourceProperty>? = null

        val currentProperty: PrefixResourceProperty?
            get() = if (current == null) null else current!!.element
    }
}
