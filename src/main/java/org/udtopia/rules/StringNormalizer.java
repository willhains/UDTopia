package org.udtopia.rules;

import org.udtopia.Value;

/**
 * Normalize  raw string values.
 */
public @Value interface StringNormalizer extends StringRule
{
	@Override default String applyTo(final Class<?> target, final String value) { return normalize(value); }

	/**
	 * Apply this rule to normalize the raw value.
	 *
	 * @param value the raw value to normalize.
	 * @return the normalized value.
	 */
	String normalize(String value);
}
