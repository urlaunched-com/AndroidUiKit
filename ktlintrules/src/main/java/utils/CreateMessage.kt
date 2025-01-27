package utils


fun createMessage(text: String, ruleId: String) = "$text\n${DISABLE_RULE_TEXT.format(ruleId)}"

private const val KTLINT_PREFIX = "ktlint:"
private const val DISABLE_RULE_TEXT = "You can disable current rule with annotation @Suppress(\"$KTLINT_PREFIX%s\")"