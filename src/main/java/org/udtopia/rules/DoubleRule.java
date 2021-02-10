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
}
