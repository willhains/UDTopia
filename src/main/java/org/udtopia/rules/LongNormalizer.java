package org.udtopia.rules;

import org.udtopia.Value;

/**
 * Normalize  raw long values.
 */
public @Value interface LongNormalizer extends LongRule
{
	@Override default long applyTo(final Class<?> target, final long value) { return normalize(value); }

	/**
	 * Apply this rule to normalize the raw value.
	 *
	 * @param value the raw value to normalize.
	 * @return the normalized value.
	 */
	long normalize(long value);
}
