package org.udtopia.rules;

import org.udtopia.Value;

/**
 * Normalize and/or validate raw string values.
 */
public @Value interface StringRule
{
	/**
	 * Applies this rule to the raw value.
	 *
	 * @param target The class annotated with this rule.
	 * @param value The raw value to which to apply this rule.
	 * @return The resulting value.
	 * @throws ValidationException if this rule applies validation that the raw value fails.
	 */
	String applyTo(Class<?> target, String value);

	/**
	 * Apply all the rules declared on an annotated class to a raw value.
	 *
	 * @param annotatedClass the class annotated with rules.
	 * @param value the value to normalize and/or validate.
	 * @return the normalized value.
	 * @throws ValidationException if this rule applies validation that the raw value fails.
	 */
	static String applyRulesFor(final Class<?> annotatedClass, final String value)
	{
		return RULES.get(annotatedClass).applyTo(annotatedClass, value);
	}

	/** Rule that does nothing. */
	StringRule NULL = (target, value) -> value;

	/** Cache of {@link StringRule}s for each annotated class. */
	RulesCache<StringRule> RULES = new RulesCache<>(StringRule.class, NULL, StringRule.Chain::together);

	/**
	 * A chain of {@link StringRule}s.
	 */
	final @Value class Chain implements StringRule
	{
		private final StringRule _rule1, _rule2;

		private Chain(final StringRule rule1, final StringRule rule2)
		{
			_rule1 = rule1;
			_rule2 = rule2;
		}

		static StringRule together(final StringRule rule1, final StringRule rule2)
		{
			if (rule1 == NULL) { return rule2; }
			if (rule2 == NULL) { return rule1; }
			return new Chain(rule1, rule2);
		}

		@Override public String applyTo(final Class<?> target, final String value)
		{
			final String resultOfRule1 = _rule1.applyTo(target, value);
			return _rule2.applyTo(target, resultOfRule1);
		}

		@Override public String toString() { return _rule1 + " -> " + _rule2; }
	}
}
