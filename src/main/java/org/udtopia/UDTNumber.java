package org.udtopia;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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

	/**
	 * @param formatter the desired format.
	 * @return a string representation of the raw value, with the specified formatter.
	 */
	String format(NumberFormat formatter);

	/**
	 * @param pattern a {@link DecimalFormat}-compatible format pattern.
	 * @return a string representation of the raw value, using the specified format pattern.
	 */
	default String format(final String pattern) { return format(new DecimalFormat(pattern)); }
}
