package io.gitlab.arturbosch.detekt.formatting

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.api.TokenRule
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.psi.KtAnnotatedExpression
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtVariableDeclaration

/**
 * @author Artur Bosch
 */
class SpacingAroundColon(config: Config) : TokenRule(config) {

	override val issue = Issue(javaClass.simpleName, Severity.Style, "", Debt.FIVE_MINS)

	override fun visitColon(colon: LeafPsiElement) {
		val parent = colon.parent
		val modified = when {
			parent is KtAnnotatedExpression -> false
			parent is KtVariableDeclaration -> colon.trimSpacesAfter(autoCorrect)
			parent is KtParameter -> colon.trimSpacesAfter(autoCorrect)
			parent is KtNamedFunction -> colon.trimSpacesAfter(autoCorrect)
			parent is KtObjectDeclaration && parent.name == null -> colon.trimSpacesAround(autoCorrect)
			parent is KtClass -> colon.trimSpacesAround(autoCorrect)
			else -> false
		}
		if (modified) {
			report(CodeSmell(issue, Entity.from(colon)))
		}
	}

}
