package com.github.syuchan1005.gitprefix.git.commit

import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceProperty
import com.github.syuchan1005.gitprefix.util.PrefixResourceFileUtil
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.actionSystem.ex.ComboBoxAction
import com.intellij.psi.SmartPointerManager
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.ui.JBColor
import com.intellij.util.ui.JBInsets
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JComponent
import javax.swing.JPanel

class GitCommitComboBoxAction : ComboBoxAction() {
    private lateinit var boxButton: ComboBoxButton

    fun setCurrent(property: SmartPsiElementPointer<PrefixResourceProperty>?) {
        current = property
        if (current == null) {
            boxButton.text = "NO PREFIX"
            boxButton.icon = null
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
        val panel = JPanel(GridBagLayout())

        presentation.setText("NO PREFIX")
        boxButton = ComboBoxButton(presentation)
        boxButton.preferredSize = Dimension(65, boxButton.height)

        val constraints = GridBagConstraints(
                0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, JBInsets.create(0, 0), 0, 0)
        panel.add(boxButton, constraints)
        panel.background = JBColor.WHITE
        return panel
    }

    companion object {
        private var current: SmartPsiElementPointer<PrefixResourceProperty>? = null

        val currentProperty: PrefixResourceProperty?
            get() = if (current == null) null else current!!.element
    }
}
