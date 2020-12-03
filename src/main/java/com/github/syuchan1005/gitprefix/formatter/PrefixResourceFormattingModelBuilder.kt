package com.github.syuchan1005.gitprefix.formatter

import com.github.syuchan1005.gitprefix.filetype.PrefixResourceLanguage
import com.github.syuchan1005.gitprefix.grammar.psi.PrefixResourceTypes
import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.TokenSet
import org.jetbrains.annotations.ApiStatus

class PrefixResourceFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel =
        FormattingModelProvider
            .createFormattingModelForPsiFile(
                formattingContext.psiElement.containingFile,
                PrefixResourceBlock(
                    formattingContext.psiElement.node,
                    Wrap.createWrap(WrapType.NONE, false),
                    Alignment.createAlignment(),
                    createSpaceBuilder(formattingContext.codeStyleSettings)
                ),
                formattingContext.codeStyleSettings
            )

    override fun getRangeAffectingIndent(file: PsiFile, offset: Int, elementAtOffset: ASTNode): TextRange? {
        return null
    }

    companion object {
        private fun createSpaceBuilder(settings: CodeStyleSettings): SpacingBuilder {
            return SpacingBuilder(settings, PrefixResourceLanguage.INSTANCE)
                .around(PrefixResourceTypes.INNER_BLOCK).spaces(1) // >name -> > name
                .around(PrefixResourceTypes.EXPAND_BLOCK).spaces(0) // ... name -> ...name
                .around(PrefixResourceTypes.BLOCK_NAME).spaces(1).before(PrefixResourceTypes.LEFT_BRACE)
                .none() // name{ -> name {
                .around(TokenSet.create(PrefixResourceTypes.EMOJI_KEY, PrefixResourceTypes.TEXT_KEY))
                .spaces(1) // :aa:t -> :aa: t
                .around(PrefixResourceTypes.NAMED_BLOCK)
                .blankLines(1) // block {\n}block {\n} -> block {\n}\n\nblock {\n}
                .around(PrefixResourceTypes.PROPERTY).lineBreakOrForceSpace(true, false)
                .around(PrefixResourceTypes.LEFT_BRACE).lineBreakInCode()
        }
    }
}
