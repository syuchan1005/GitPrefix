package com.github.syuchan1005.gitprefix.util

import com.github.syuchan1005.gitprefix.GitPrefixData
import com.github.syuchan1005.gitprefix.grammar.PrefixResourceElementFactory
import com.github.syuchan1005.gitprefix.grammar.PrefixResourceFile
import com.github.syuchan1005.gitprefix.grammar.mixin.PrefixResourceBlockExprMixin.BlockExprType
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceBlockExpr
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceNamedBlock
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceProperty
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes
import com.intellij.codeInsight.actions.ReformatCodeProcessor
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.JBMenuItem
import com.intellij.openapi.ui.JBPopupMenu
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.tree.TokenSet
import java.awt.Container
import java.awt.event.ActionEvent
import java.util.*
import java.util.function.Consumer
import javax.swing.JMenu

object PrefixResourceFileUtil {
    private val needElements = TokenSet.create(PrefixResourceTypes.PROPERTY, PrefixResourceTypes.NAMED_BLOCK)

    @JvmStatic
	fun createStructuredFile(prefixResourceFile: PrefixResourceFile, isPreview: Boolean): PrefixResourceFile {
        var file = prefixResourceFile.copy() as PrefixResourceFile

        /* extract property, calcNamedBlock */
        val properties: MutableList<PrefixResourceProperty> = ArrayList()
        for (child in file.children) {
            if (child is PrefixResourceProperty) {
                properties.add(child)
                file.node.removeChild(child.getNode())
            } else if (child is PrefixResourceNamedBlock) {
                calcNamedBlock(child)
            } else if (!needElements.contains(child.node.elementType)) {
                file.node.removeChild(child.node)
            }
        }

        /* remove blocks */for (child in file.children) {
            if (child is PrefixResourceNamedBlock) {
                val blockName = child.blockName
                val fromBeforeName = BlockType.getFromBeforeName(blockName.text)
                if (fromBeforeName != null) {
                    child.getNode().replaceChild(blockName.node,
                            PrefixResourceElementFactory.createBlockNameNode(file.project, fromBeforeName.blockName))
                    properties.forEach(Consumer { n: PrefixResourceProperty -> child.getNode().addChild(n.copy().node, child.getNode().lastChildNode.treePrev) })
                } else {
                    file.node.removeChild(child.getNode())
                }
            }
        }
        for (value in BlockType.values()) {
            if (value.getBlock(file) == null) {
                val namedBlock = PrefixResourceElementFactory.createNamedBlock(file.project, value.blockName)
                file.node.addChild(namedBlock.node)
                properties.forEach(Consumer { n: PrefixResourceProperty -> namedBlock.node.addChild(n.copy().node, namedBlock.node.lastChildNode.treePrev) })
            }
        }
        if (isPreview) {
            for (child in file.children) {
                addSpaceAfterProperty(child as PrefixResourceNamedBlock)
            }
            file = PrefixResourceElementFactory.createFile(file.project, file.text.replace("\n\\s*?\n".toRegex(), "\n"))
            ReformatCodeProcessor(file, false).runWithoutProgress()
        }
        return file
    }

    private fun calcNamedBlock(block: PrefixResourceNamedBlock) {
        for (child in block.children) {
            if (!(child is PrefixResourceBlockExpr || child is PrefixResourceNamedBlock || child is PrefixResourceProperty)) block.node.removeChild(child.node)
        }
        for (child in block.children) {
            if (child is PrefixResourceBlockExpr) {
                val expr = child
                val targetNamedBlock = expr.targetNamedBlock
                if (targetNamedBlock == null || PrefixPsiUtil.isRecursive(expr)) {
                    block.node.removeChild(expr.node)
                } else {
                    val newChild = targetNamedBlock.node.copyElement()
                    when (expr.exprType) {
                        BlockExprType.EXPAND -> {
                            for (node in newChild.getChildren(needElements)) {
                                block.node.addChild(node, expr.node)
                            }
                            block.node.removeChild(expr.node)
                        }
                        BlockExprType.INNER -> block.node.replaceChild(expr.node, newChild)
                        BlockExprType.UNKNOWN -> {}
                    }
                }
            } else if (child is PrefixResourceNamedBlock) {
                calcNamedBlock(child)
            }
        }
    }

    private fun addSpaceAfterProperty(block: PrefixResourceNamedBlock) {
        for (child in block.children) {
            if (child is PrefixResourceProperty) block.node.addChild(PrefixResourceElementFactory.createLF(block.project).node, child.getNode()) else if (child is PrefixResourceNamedBlock) addSpaceAfterProperty(child)
        }
    }

    fun getFromSetting(project: Project): PrefixResourceFile? {
        val prefixData = ServiceManager.getService(project, GitPrefixData::class.java)
        val virtualFile: VirtualFile?
        virtualFile = if (prefixData.pathType == GitPrefixData.PathType.DEFAULT) {
            val basePath = project.basePath ?: return null
            val baseDir = LocalFileSystem.getInstance().refreshAndFindFileByPath(basePath) ?: return null
            baseDir.findChild(".gitprefix")
        } else if (prefixData.pathType == GitPrefixData.PathType.CUSTOM && !prefixData.gitPrefixPath.isEmpty()) {
            LocalFileSystem.getInstance().refreshAndFindFileByPath(prefixData.gitPrefixPath)
        } else {
            return null
        }
        if (virtualFile == null) return null
        val psiFile = PsiManager.getInstance(project).findFile(virtualFile)
        return if (psiFile !is PrefixResourceFile) null else psiFile
    }

    enum class BlockType(private val beforeName: String, val blockName: String) {
        COMMIT("commit", "\$commit"),
        MERGE("merge", "\$merge"),
        TAG("tag", "\$tag");

        fun getBlock(file: PrefixResourceFile): PrefixResourceNamedBlock? {
            // PrefixResourceFile structuredFile = PrefixResourceFileUtil.createStructuredFile(file, false);
            for (child in file.children) {
                if (child is PrefixResourceNamedBlock && blockName == child.name) {
                    return child
                }
            }
            return null
        }

        fun createActionGroup(file: PrefixResourceFile?, click: (PrefixResourceProperty) -> Unit): DefaultActionGroup {
            val group = DefaultActionGroup()
            if (file != null) {
                val block = getBlock(createStructuredFile(file, false))
                block?.let { addFileContent(it, group, click) }
            }
            return group
        }

        fun createPopupMenu(file: PrefixResourceFile?, click: (PrefixResourceProperty) -> Unit): JBPopupMenu? {
            if (file == null) return null
            val block = getBlock(createStructuredFile(file, false)) ?: return null
            val popupMenu = JBPopupMenu()
            return if (a(block, popupMenu, click) == 0) null else popupMenu
        }

        companion object {
            private fun toAction(property: PrefixResourceProperty, click: (PrefixResourceProperty) -> Unit): AnAction {
                val emoji = property.emoji
                val action: AnAction
                action = if (emoji != null) {
                    object : AnAction(property.valueText, null, emoji.icon) {
                        override fun actionPerformed(e: AnActionEvent) {
                            click(property)
                        }
                    }
                } else {
                    object : AnAction("|" + property.key + "| " + property.valueText) {
                        override fun actionPerformed(e: AnActionEvent) {
                            click(property)
                        }
                    }
                }
                return action
            }

            private fun toActionGroup(block: PrefixResourceNamedBlock, click: (PrefixResourceProperty) -> Unit): DefaultActionGroup {
                val group = DefaultActionGroup(block.name, true)
                addFileContent(block, group, click)
                return group
            }

            private fun addFileContent(block: PrefixResourceNamedBlock, group: DefaultActionGroup, click: (PrefixResourceProperty) -> Unit) {
                for (child in block.children) {
                    if (child is PrefixResourceProperty) {
                        group.add(toAction(child, click))
                    } else if (child is PrefixResourceNamedBlock) {
                        group.add(toActionGroup(child, click))
                    }
                }
            }

            private fun toMenuItem(property: PrefixResourceProperty, click: (PrefixResourceProperty) -> Unit): JBMenuItem {
                val emoji = property.emoji
                val menuItem: JBMenuItem
                menuItem = if (emoji != null) {
                    JBMenuItem(property.valueText, emoji.icon)
                } else {
                    JBMenuItem("|" + property.key + "| " + property.valueText)
                }
                menuItem.addActionListener { _ -> click(property) }
                return menuItem
            }

            private fun toJMenu(block: PrefixResourceNamedBlock, click: (PrefixResourceProperty) -> Unit): JMenu? {
                val menu = JMenu(block.name)
                return if (a(block, menu, click) == 0) null else menu
            }

            private fun a(block: PrefixResourceNamedBlock, container: Container, click: (PrefixResourceProperty) -> Unit): Int {
                var addCount = 0
                for (child in block.children) {
                    if (child is PrefixResourceProperty) {
                        addCount++
                        container.add(toMenuItem(child, click))
                    } else if (child is PrefixResourceNamedBlock) {
                        val menu = toJMenu(child, click)
                        if (menu != null) addCount++
                        container.add(menu)
                    }
                }
                return addCount
            }

            fun getFromBeforeName(name: String): BlockType? {
                for (value in values()) {
                    if (value.beforeName == name) return value
                }
                return null
            }
        }

    }
}