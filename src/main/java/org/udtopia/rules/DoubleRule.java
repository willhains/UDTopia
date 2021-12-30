package org.udtopia.rules;

import org.udtopia.Value;

/**
 * Normalize and/or validate raw double values.
 */
public @Value interface DoubleRule
{
	/**
	 * Applies this rule to the raw value.
	 *
	 * @param target The class annotated with this rule.
	 * @param value The raw value to which to apply this rule.
	 * @return The resulting value.
	 * @throws ValidationException if this rule applies validation that the raw value fails.
	 */
	double applyTo(Class<?> target, double value);

	/**
	 * Apply all the rules declared on an annotated class to a raw value.
	 *
	 * @param annotatedClass the class annotated with rules.
	 * @param value the value to normalize and/or validate.
	 * @return the normalized value.
	 * @throws ValidationException if this rule applies validation that the raw value fails.
	 */
	static double applyRulesFor(final Class<?> annotatedClass, final double value)
	{
		return RULES.get(annotatedClass).applyTo(annotatedClass, value);
	}

	/** Rule that does nothing. */
	DoubleRule NULL = (target, value) -> value;

	/** Cache of {@link DoubleRule}s for each annotated class. */
	RulesCache<DoubleRule> RULES = new RulesCache<>(DoubleRule.class, NULL, Chain::together);

	/**
	 * A chain of {@link DoubleRule}s.
	 */
	final @Value class Chain implements DoubleRule
	{
		private final DoubleRule _rule1, _rule2;

		private Chain(final DoubleRule rule1, final DoubleRule rule2)
		{
			_rule1 = rule1;
			_rule2 = rule2;
		}

		static DoubleRule together(final DoubleRule rule1, final DoubleRule rule2)
		{
			if (rule1 == NULL) { return rule2; }
			if (rule2 == NULL) { return rule1; }
			return new Chain(rule1, rule2);
		}

		@Override public double applyTo(final Class<?> target, final double value)
		{
			final double resultOfRule1 = _rule1.applyTo(target, value);
			return _rule2.applyTo(target, resultOfRule1);
		}

		@Override public String toString() { return _rule1 + " -> " + _rule2; }
	}
}
