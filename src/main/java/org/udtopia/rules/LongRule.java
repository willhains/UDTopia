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
}
