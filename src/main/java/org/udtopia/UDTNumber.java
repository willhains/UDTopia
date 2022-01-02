package org.udtopia;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

/**
 * A wrapped numeric value.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public @Value interface UDTNumber<This extends UDTNumber<This>>
	extends UDTComparable<This>, IntSupplier, LongSupplier, DoubleSupplier
{
	/** @return {@code true} if the raw value is zero. */
	boolean isZero();

	/** @return {@code false} if the raw value is zero. */
	default boolean isNonZero() { return !isZero(); }

	/** @return {@code true} if the raw value is non-zero positive. */
	boolean isPositive();

	/** @return {@code true} if the raw value is non-zero negative. */
	boolean isNegative();
}
