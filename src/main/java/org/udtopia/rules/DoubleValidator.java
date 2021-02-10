package org.udtopia.rules;

import org.udtopia.Value;

/**
 * Validate raw double values.
 */
public @Value interface DoubleValidator extends DoubleRule
{
	@Override default double applyTo(final Class<?> target, final double value)
	{
		validate(target, value);
		return value;
	}

	/**
	 * Apply this rule to validate the raw value.
	 *
	 * @param target the class annotated with this rule.
	 * @param value the value to validate.
	 * @throws ValidationException if this rule applies validation that the raw value fails.
	 */
	void validate(Class<?> target, double value);
}
