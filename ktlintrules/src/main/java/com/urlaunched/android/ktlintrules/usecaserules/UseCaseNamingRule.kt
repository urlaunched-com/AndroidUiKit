package com.urlaunched.android.ktlintrules.usecaserules

import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.rule.engine.core.api.AutocorrectDecision
import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleAutocorrectApproveHandler
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtClass

class UseCaseNamingRule :
    Rule(
        RuleId(CUSTOM_RULE_ID),
        about = About()
    ),
    RuleAutocorrectApproveHandler {
    override fun beforeVisitChildNodes(
        node: ASTNode,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> AutocorrectDecision
    ) {
        if (node.elementType == ElementType.CLASS) {
            val psiClass = node.psi as? KtClass ?: return

            val packageName = psiClass.containingKtFile.packageFqName.asString()
            if (packageName.endsWith(USE_CASE_PACKAGE)) {
                val className = psiClass.name ?: return
                if (!className.endsWith(USE_CASE_POSTFIX)) {
                    emit(
                        node.startOffset,
                        ERROR_MESSAGE.format(className, packageName),
                        false
                    )
                }
            }
        }
    }

    companion object {
        private const val CUSTOM_RULE_ID = "ktlintrules:usecasenamingrule"
        private const val USE_CASE_PACKAGE = ".domain.usecases"
        private const val USE_CASE_POSTFIX = "UseCase"
        private const val ERROR_MESSAGE = "Class name '%s' in '%s' should end with 'UseCase'\""
    }
}