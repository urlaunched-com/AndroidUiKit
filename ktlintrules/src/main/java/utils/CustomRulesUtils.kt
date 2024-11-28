package utils

import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtPsiUtil
import org.jetbrains.kotlin.psi.psiUtil.visibilityModifierType

object CustomRulesUtils {
    private val KtAnnotationEntry.isPreviewAnnotation: Boolean
        get() = calleeExpression?.text?.contains("Preview") == true

    val KtNamedFunction.isPreview: Boolean
        get() = annotationEntries.any { it.isPreviewAnnotation }

    val KtNamedFunction.isPublic: Boolean
        get() {
            if (KtPsiUtil.isLocal(this)) return false
            val visibilityModifier = visibilityModifierType()
            return visibilityModifier == null || visibilityModifier == KtTokens.PUBLIC_KEYWORD
        }

    val KtNamedFunction.isPrivate: Boolean
        get() {
            if (KtPsiUtil.isLocal(this)) return false
            val visibilityModifier = visibilityModifierType()
            return visibilityModifier == null || visibilityModifier == KtTokens.PRIVATE_KEYWORD
        }

    val KtClass.isInternal: Boolean
        get() {
            if (KtPsiUtil.isLocal(this)) return false
            val visibilityModifier = visibilityModifierType()
            return visibilityModifier == KtTokens.INTERNAL_KEYWORD
        }

    val KtNamedFunction.isComposable: Boolean
        get() = annotationEntries.any { it.calleeExpression?.text == "Composable" }
}