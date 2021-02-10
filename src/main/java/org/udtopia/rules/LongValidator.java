package org.udtopia.rules;

import org.udtopia.Value;

/**
 * Validate raw long values.
 */
public @Value interface LongValidator extends LongRule
{
	@Override default long applyTo(final Class<?> target, final long value)
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
	void validate(Class<?> target, long value);
}
