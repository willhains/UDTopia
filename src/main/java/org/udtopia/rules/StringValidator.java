package org.udtopia.rules;

import org.udtopia.Value;

/**
 * Validate raw string values.
 */
public @Value interface StringValidator extends StringRule
{
	@Override default String applyTo(final Class<?> target, final String value)
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
	void validate(Class<?> target, String value);
}
