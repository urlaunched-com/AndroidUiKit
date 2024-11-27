package com.urlaunched.android.ktlintrules

import com.pinterest.ktlint.rule.engine.core.api.AutocorrectDecision
import com.pinterest.ktlint.rule.engine.core.api.ElementType
import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleAutocorrectApproveHandler
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import org.jetbrains.kotlin.com.intellij.lang.ASTNode

class LocalizableResourcesRule :
    Rule(
        RuleId(CUSTOM_RULE_ID),
        about = About()
    ),
    RuleAutocorrectApproveHandler {
    override fun beforeVisitChildNodes(
        node: ASTNode,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> AutocorrectDecision
    ) {
        if (node.elementType == ElementType.IMPORT_LIST) {
            val importStatements = node.text.split("\n").filter { it.isNotBlank() }

            importStatements.forEach { import ->
                if ((import.contains(LOCALIZATION_IMPORT) || import.contains(LOCALIZABLE_CAST)) &&
                    !import.contains(CORRECT_IMPORT)
                ) {
                    emit(
                        node.startOffset,
                        ERROR_MESSAGE,
                        false
                    )
                }
            }
        }
    }

    companion object {
        private const val CUSTOM_RULE_ID = "ktlintrules:localizableresourses"
        private const val LOCALIZATION_IMPORT = "core.localization.R"
        private const val LOCALIZABLE_CAST = "as LocalizableResources"
        private const val CORRECT_IMPORT = "core.localization.LocalizableResources"
        private const val ERROR_MESSAGE =
            "The import for localization resources must be import ...core.localization.LocalizableResources"
    }
}