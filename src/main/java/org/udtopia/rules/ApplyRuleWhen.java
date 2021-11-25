package org.udtopia.rules;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Predicate;
import org.udtopia.Value;

/**
 * When to apply a given rule.
 */
public @Value enum ApplyRuleWhen
{
	/** Always apply the rule. */
	ALWAYS(target -> true),

	/** Apply the rule only if JVM assertions are active on the target class. */
	ASSERTS_ENABLED(Class::desiredAssertionStatus);

	private final Predicate<? super Class<?>> _condition;

	ApplyRuleWhen(final Predicate<? super Class<?>> condition) { _condition = condition; }

	/**
	 * Decide whether to apply the rule for the given annotation to the given class it annotates, based on the
	 * presence and value of an {@code ApplyRuleWhen} parameter.
	 *
	 * @param annotation the annotation declaring the rule.
	 * @param annotatedClass the class annotated by the annotation.
	 * @return {@code true} if the rule should be applied; {@code false} if it should be skipped.
	 */
	public static boolean shouldApplyRule(final Annotation annotation, final Class<?> annotatedClass)
	{
		// Search the annotation for ApplyRuleWhen
		for (final Method annotationParameter: annotation.annotationType().getDeclaredMethods())
		{
			if (annotationParameter.getReturnType().equals(ApplyRuleWhen.class))
			{
				try
				{
					// Apply condition
					final ApplyRuleWhen applyRulesWhen = (ApplyRuleWhen) annotationParameter.invoke(annotation);
					return applyRulesWhen._condition.test(annotatedClass);
				}
				catch (final IllegalAccessException | InvocationTargetException ignored)
				{
					// Ignore; this will be caught when reading the annotation parameter anyway
				}
			}
		}
		return true;
	}
}
