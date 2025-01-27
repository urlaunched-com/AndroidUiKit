package com.urlaunched.android.ktlintrules.usecaserules

import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.rule.engine.core.api.AutocorrectDecision
import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleAutocorrectApproveHandler
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import utils.createMessage

class UseCaseCallRule :
    Rule(
        RuleId(CUSTOM_RULE_ID),
        about = About()
    ),
    RuleAutocorrectApproveHandler {
    override fun beforeVisitChildNodes(
        node: ASTNode,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> AutocorrectDecision
    ) {
        if (node.elementType == ElementType.CALL_EXPRESSION) {
            val callExpression = node.psi as? KtCallExpression ?: return
            val parentExpression = callExpression.parent
            if (parentExpression is KtDotQualifiedExpression) {
                val receiverExpression = parentExpression.receiverExpression as? KtNameReferenceExpression

                if (receiverExpression != null) {
                    val receiverName = receiverExpression.getReferencedName()
                    if (receiverName.endsWith(USE_CASE_POSTFIX) &&
                        callExpression.calleeExpression?.text == INVOKE
                    ) {
                        emit(
                            node.startOffset,
                            createMessage(
                                text = ERROR_MESSAGE.format(receiverName),
                                ruleId = CUSTOM_RULE_ID
                            ),
                            false
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val CUSTOM_RULE_ID = "ktlintrules:usecase-call-rule"
        private const val ERROR_MESSAGE = "`UseCase` calls should not use `.invoke()`. Use `$%s()` instead."
        private const val USE_CASE_POSTFIX = "UseCase"
        private const val INVOKE = "invoke"
    }
}