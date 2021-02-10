package org.udtopia.rules;

import org.udtopia.Value;

/**
 * Validate raw int values.
 */
public @Value interface IntValidator extends IntRule
{
	@Override default int applyTo(final Class<?> target, final int value)
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
	void validate(Class<?> target, int value);
}
