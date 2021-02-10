package org.udtopia.rules;

import org.udtopia.Value;

/**
 * Normalize  raw double values.
 */
public @Value interface DoubleNormalizer extends DoubleRule
{
	@Override default double applyTo(final Class<?> target, final double value) { return normalize(value); }

	/**
	 * Apply this rule to normalize the raw value.
	 *
	 * @param value the raw value to normalize.
	 * @return the normalized value.
	 */
	double normalize(double value);
}
