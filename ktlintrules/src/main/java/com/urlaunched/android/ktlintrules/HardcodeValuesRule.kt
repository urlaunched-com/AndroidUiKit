package com.urlaunched.android.ktlintrules

import com.pinterest.ktlint.rule.engine.core.api.AutocorrectDecision
import com.pinterest.ktlint.rule.engine.core.api.ElementType
import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleAutocorrectApproveHandler
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.psiUtil.getParentOfType

class HardcodeValuesRule :
    Rule(
        RuleId(CUSTOM_RULE_ID),
        about = About()
    ),
    RuleAutocorrectApproveHandler {
    override fun beforeVisitChildNodes(
        node: ASTNode,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> AutocorrectDecision
    ) {
        when (node.elementType) {
            ElementType.CALL_EXPRESSION -> {
                val callExpression = node.psi as? KtCallExpression ?: return
                val parentFunction = callExpression.getParentOfType<KtNamedFunction>(strict = false)

                if (parentFunction != null && isComposableFunction(parentFunction)) {
                    callExpression.valueArguments.forEach { argument ->
                        val argumentExpression = argument.getArgumentExpression()
                        if (isHardcodedValue(argumentExpression?.text)) {
                            emit(
                                argument.node.startOffset,
                                ERROR_MESSAGE_TEXT.format(argumentExpression?.text),
                                false
                            )
                        }
                    }
                }
            }

            ElementType.PROPERTY -> {
                val property = node.psi as? KtProperty ?: return
                val parentFunction = property.getParentOfType<KtNamedFunction>(strict = false)

                if (parentFunction != null && isComposableFunction(parentFunction)) {
                    if (isHardcodedValue(property.initializer?.text)) {
                        emit(
                            property.node.startOffset,
                            ERROR_MESSAGE_TEXT.format(property.initializer?.text),
                            false
                        )
                    }
                }
            }
        }
    }

    private fun isComposableFunction(function: KtNamedFunction): Boolean {
        if (function.name?.contains("Shimmers", ignoreCase = true) == true ||
            function.name?.contains("Shimmer", ignoreCase = true) == true
        ) {
            return false
        }

        val annotations = function.annotationEntries.map { it.text }
        return annotations.any { it.contains("@Composable") } && annotations.none { it.contains("@Preview") }
    }

    private fun isHardcodedValue(value: String?): Boolean {
        if (value.isNullOrEmpty()) return false
        if (value.contains(Regex("\\$\\{.*?}"))) return false
        return value.matches(Regex("\".*\"|\\d+(\\.\\d+)?\\.(dp|px)"))
    }

    companion object {
        private const val CUSTOM_RULE_ID = "ktlintrules:forbidenhardcodevalues"
        private val ERROR_MESSAGE_TEXT = "Avoid hardcoding values like `%s` in composable function parameters."
    }
}