package org.udtopia.rules;

import org.udtopia.Value;

/**
 * Normalize and/or validate raw int values.
 */
public @Value interface IntRule
{
	/**
	 * Applies this rule to the raw value.
	 *
	 * @param target The class annotated with this rule.
	 * @param value The raw value to which to apply this rule.
	 * @return The resulting value.
	 * @throws ValidationException if this rule applies validation that the raw value fails.
	 */
	int applyTo(Class<?> target, int value);

	/**
	 * Apply all the rules declared on an annotated class to a raw value.
	 *
	 * @param annotatedClass the class annotated with rules.
	 * @param value the value to normalize and/or validate.
	 * @return the normalized value.
	 * @throws ValidationException if this rule applies validation that the raw value fails.
	 */
	static int applyRulesFor(final Class<?> annotatedClass, final int value)
	{
		return RULES.get(annotatedClass).applyTo(annotatedClass, value);
	}

	/** Rule that does nothing. */
	IntRule NULL = (target, value) -> value;

	/** Cache of {@link IntRule}s for each annotated class. */
	RulesCache<IntRule> RULES = new RulesCache<>(IntRule.class, NULL, IntRule.Chain::together);

	/**
	 * A chain of {@link IntRule}s.
	 */
	final @Value class Chain implements IntRule
	{
		private final IntRule _rule1, _rule2;

		private Chain(final IntRule rule1, final IntRule rule2)
		{
			_rule1 = rule1;
			_rule2 = rule2;
		}

		static IntRule together(final IntRule rule1, final IntRule rule2)
		{
			if (rule1 == NULL) { return rule2; }
			if (rule2 == NULL) { return rule1; }
			return new Chain(rule1, rule2);
		}

		@Override public int applyTo(final Class<?> target, final int value)
		{
			final int resultOfRule1 = _rule1.applyTo(target, value);
			return _rule2.applyTo(target, resultOfRule1);
		}

		@Override public String toString() { return _rule1 + " -> " + _rule2; }
	}
}
