package com.github.syuchan1005.gitprefix.util

import com.github.syuchan1005.gitprefix.grammar.PrefixResourceFile
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceBlockExpr
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceNamedBlock
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceProperty
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import java.util.*

object PrefixPsiUtil {
    @JvmStatic
	fun getChildrenRecursive(root: ASTNode, filter: TokenSet?): List<ASTNode> {
        val result: MutableList<ASTNode> = ArrayList()
        val children = root.getChildren(filter)
        for (child in children) {
            result.add(child)
            val children1 = child.getChildren(filter)
            if (children1.isNotEmpty()) result.addAll(getChildrenRecursive(child, filter))
        }
        return result
    }

    private fun getContainsRootNamedBlock(element: PsiElement): PrefixResourceNamedBlock? {
        val parent = element.parent
        return if (parent is PrefixResourceFile) {
            if (element is PrefixResourceNamedBlock) element else null
        } else {
            getContainsRootNamedBlock(parent)
        }
    }

    private fun getExprRecursive(block: PrefixResourceNamedBlock): List<PrefixResourceBlockExpr> {
        val result: MutableList<PrefixResourceBlockExpr> = ArrayList(block.blockExprList)
        for (namedBlock in block.namedBlockList) {
            result.addAll(getExprRecursive(namedBlock))
        }
        return result
    }

    private fun getTargetBlockRecursive(targetBlocks: List<PrefixResourceNamedBlock?>,
                                        targetNamedBlock: PrefixResourceNamedBlock?): List<PrefixResourceNamedBlock?> {
        val result: MutableList<PrefixResourceNamedBlock?> = ArrayList(targetBlocks)
        if (targetNamedBlock == null) return result
        val exprRecursive = getExprRecursive(targetNamedBlock)
        for (blockExpr in exprRecursive) {
            val namedBlock = blockExpr.targetNamedBlock
            if (result.contains(namedBlock)) continue
            result.add(namedBlock)
            result.addAll(getTargetBlockRecursive(result, namedBlock))
        }
        return result
    }

    @JvmStatic
	fun isRecursive(expr: PrefixResourceBlockExpr): Boolean {
        val containsRootNamedBlock = getContainsRootNamedBlock(expr)
        val targetNamedBlock = expr.targetNamedBlock
        if (containsRootNamedBlock == null || targetNamedBlock == null) return false
        if (containsRootNamedBlock === targetNamedBlock) return true

        // nest recursive
        val targetBlocks = getTargetBlockRecursive(emptyList(), targetNamedBlock)
        return targetBlocks.contains(containsRootNamedBlock)
    }
}

fun PrefixResourceNamedBlock?.getFlattenPropertyList(): List<PrefixResourceProperty> {
    if (this == null) return emptyList()
    val properties = mutableListOf<PrefixResourceProperty>()
    this.children.forEach { child ->
        if (child is PrefixResourceProperty) {
            properties.add(child)
        } else if (child is PrefixResourceNamedBlock) {
            properties.addAll(child.getFlattenPropertyList())
        }
    }
    return properties
}