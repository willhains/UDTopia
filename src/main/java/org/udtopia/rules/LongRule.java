package org.udtopia.rules;

import org.udtopia.Value;

/**
 * Normalize and/or validate raw long values.
 */
public @Value interface LongRule
{
	/**
	 * Applies this rule to the raw value.
	 *
	 * @param target The class annotated with this rule.
	 * @param value The raw value to which to apply this rule.
	 * @return The resulting value.
	 * @throws ValidationException if this rule applies validation that the raw value fails.
	 */
	long applyTo(Class<?> target, long value);

	/**
	 * Apply all the rules declared on an annotated class to a raw value.
	 *
	 * @param annotatedClass the class annotated with rules.
	 * @param value the value to normalize and/or validate.
	 * @return the normalized value.
	 * @throws ValidationException if this rule applies validation that the raw value fails.
	 */
	static long applyRulesFor(final Class<?> annotatedClass, final long value)
	{
		return RULES.get(annotatedClass).applyTo(annotatedClass, value);
	}

	/** Rule that does nothing. */
	LongRule NULL = (target, value) -> value;

	/** Cache of {@link LongRule}s for each annotated class. */
	RulesCache<LongRule> RULES = new RulesCache<>(LongRule.class, NULL, LongRule.Chain::together);

	/**
	 * A chain of {@link LongRule}s.
	 */
	final @Value class Chain implements LongRule
	{
		private final LongRule _rule1, _rule2;

		private Chain(final LongRule rule1, final LongRule rule2)
		{
			_rule1 = rule1;
			_rule2 = rule2;
		}

		static LongRule together(final LongRule rule1, final LongRule rule2)
		{
			if (rule1 == NULL) { return rule2; }
			if (rule2 == NULL) { return rule1; }
			return new Chain(rule1, rule2);
		}

		@Override public long applyTo(final Class<?> target, final long value)
		{
			final long resultOfRule1 = _rule1.applyTo(target, value);
			return _rule2.applyTo(target, resultOfRule1);
		}

		@Override public String toString() { return _rule1 + " -> " + _rule2; }
	}
}
