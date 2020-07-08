package com.github.syuchan1005.gitprefix.editor

import com.github.syuchan1005.gitprefix.EmojiUtil.Companion.emojiMap
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement

class PrefixIconLineMarkerProvider : RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(element: PsiElement, result: MutableCollection<in RelatedItemLineMarkerInfo<*>?>) {
        if (element is LeafPsiElement) {
            var lineMarkerInfo: RelatedItemLineMarkerInfo<PsiElement?>? = null
            if (element.elementType === PrefixResourceTypes.KEY_TEXT &&
                    element.node.treePrev.elementType === PrefixResourceTypes.EMOJI_WRAP) {
                val emoji = element.text
                if (emoji.length < 1) return
                val emojiData = emojiMap[emoji]
                if (emojiData != null) {
                    val builder = NavigationGutterIconBuilder.create(emojiData.icon).setTarget(element)
                    lineMarkerInfo = builder.createLineMarkerInfo(element)
                }
            }
            if (lineMarkerInfo != null) result.add(lineMarkerInfo)
        }
    }
}