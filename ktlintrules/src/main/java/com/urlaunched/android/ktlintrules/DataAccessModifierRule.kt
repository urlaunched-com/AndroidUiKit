package com.urlaunched.android.ktlintrules

import com.pinterest.ktlint.rule.engine.core.api.AutocorrectDecision
import com.pinterest.ktlint.rule.engine.core.api.ElementType
import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleAutocorrectApproveHandler
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.psiUtil.isPublic
import utils.CustomRulesUtils.isInternal

class DataAccessModifierRule :
    Rule(
        RuleId(CUSTOM_RULE_ID),
        about = About()
    ),
    RuleAutocorrectApproveHandler {
    override fun beforeVisitChildNodes(
        node: ASTNode,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> AutocorrectDecision
    ) {
        if (node.elementType == ElementType.CLASS || node.elementType == ElementType.INTERFACE_KEYWORD) {
            val isInDataPackage = (node.psi.containingFile as? KtFile)
                ?.packageFqName
                ?.asString()
                ?.contains(DATA_PACKAGE_NAME) == true

            if (!isInDataPackage) return
            val clazz = node.psi as? KtClass ?: return

            if (clazz.isPublic) {
                emit(
                    node.startOffset,
                    DATA_ACCESS_ERROR,
                    false
                )
            }
        }
    }

    companion object {
        private const val CUSTOM_RULE_ID = "ktlintrules:data-access-modifier-rule"
        private const val DATA_PACKAGE_NAME = "data"
        private const val DATA_ACCESS_ERROR = "Classes and Interfaces in 'data' package should not be public."
    }
}