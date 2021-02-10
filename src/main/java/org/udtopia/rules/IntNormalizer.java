package org.udtopia.rules;

import org.udtopia.Value;

/**
 * Normalize  raw int values.
 */
public @Value interface IntNormalizer extends IntRule
{
	@Override default int applyTo(final Class<?> target, final int value) { return normalize(value); }

	/**
	 * Apply this rule to normalize the raw value.
	 *
	 * @param value the raw value to normalize.
	 * @return the normalized value.
	 */
	int normalize(int value);
}
