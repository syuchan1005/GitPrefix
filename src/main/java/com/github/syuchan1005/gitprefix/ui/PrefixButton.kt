package com.github.syuchan1005.gitprefix.ui

import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceProperty
import com.github.syuchan1005.gitprefix.util.PrefixResourceFileUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.JBMenuItem
import com.intellij.openapi.ui.JBPopupMenu
import com.intellij.psi.SmartPointerManager
import com.intellij.psi.SmartPsiElementPointer
import java.awt.event.ActionEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JButton

class PrefixButton(private val myProject: Project, private val myHolder: TextHolder) : JButton() {
    var popupMenu: JBPopupMenu? = null
        private set

    private var current: SmartPsiElementPointer<PrefixResourceProperty>? = null

    fun settingPopup(type: PrefixResourceFileUtil.BlockType) {
        popupMenu = type.createPopupMenu(PrefixResourceFileUtil.getFromSetting(myProject)
        ) { p: PrefixResourceProperty -> setCurrent(SmartPointerManager.getInstance(p.project).createSmartPsiElementPointer(p)) }
        if (popupMenu == null) return
        JBMenuItem("NO PREFIX").apply {
            addActionListener { setCurrent(null) }
            popupMenu!!.add(this)
        }
    }

    fun setCurrent(current: SmartPsiElementPointer<PrefixResourceProperty>?) {
        val key = if (this.current != null) this.current!!.element!!.key else null
        this.current = current
        if (current == null) {
            icon = null
            text = "NO PREFIX"
        } else {
            val p = current.element ?: return
            val emoji = p.emoji
            if (emoji != null) {
                icon = emoji.icon
                text = p.valueText
            } else {
                icon = null
                text = "|" + p.key + "| " + p.valueText
            }
        }
        var commitMessage = myHolder.getText()
        if (key != null && commitMessage.startsWith(key)) {
            commitMessage = commitMessage.substring(key.length)
        }
        val trimMessage = commitMessage.replace("^\\s+".toRegex(), "")
        if (current == null) {
            myHolder.setText(trimMessage)
        } else {
            myHolder.setText(current.element!!.key + " " + trimMessage)
        }
    }

    val currentProperty: PrefixResourceProperty?
        get() = if (current == null) null else current!!.element

    abstract class TextHolder {
        abstract fun getText(): String
        abstract fun setText(text: String?)
    }

    init {
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                popupMenu!!.show(e.component, e.x, e.y)
            }
        })
        text = "NO PREFIX"
    }
}
