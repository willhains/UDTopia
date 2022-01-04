package org.udtopia;

import java.util.function.DoubleSupplier;
import javax.annotation.Nullable;
import org.udtopia.assertion.Assert;

/**
 * A value type wrapping a primitive {@code double}.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class UDTDouble<This extends UDTDouble<This>> implements UDTNumber<This>
{
	/**
	 * If the raw value can be exactly represented by an {@code int}, convert it.
	 *
	 * @return the raw value as an {@code int}.
	 * @throws ArithmeticException if the raw value has a fractional part, or is outside the range of {@code int}.
	 */
	@Override public final int getAsInt()
	{
		final double raw = getAsDouble();
		final int rawAsInt = (int) raw;
		if (rawAsInt != raw) { throw new ArithmeticException("Cannot exactly represent as int: " + this); }
		return rawAsInt;
	}

	/**
	 * If the raw value can be exactly represented by a {@code long}, convert it.
	 *
	 * @return the raw value as a {@code long}.
	 * @throws ArithmeticException if the raw value has a fractional part, or is outside the range of {@code long}.
	 */
	@Override public final long getAsLong()
	{
		final double raw = getAsDouble();
		final long rawAsLong = (long) raw;
		if (rawAsLong != raw) { throw new ArithmeticException("Cannot exactly represent as long: " + this); }
		return rawAsLong;
	}

	/** @return the raw value. */
	@Override public abstract double getAsDouble();

	/**
	 * Convert the raw value to an {@code int} without throwing.
	 * Unlike {@link #getAsInt()}, this method will <b>change the value</b> to fit within the range of {@code int},
	 * and remove the fractional part of the value.
	 *
	 * @return the raw value rounded to the closest {@code int} value.
	 * @see UDTInt#nearestInt
	 */
	public final int roundToInt() { return UDTInt.nearestInt(roundToLong()); }

	/**
	 * Convert the raw value to a {@code long} without throwing.
	 * Unlike {@link #getAsLong()}, this method will <b>change the value</b> to fit within the range of {@code long},
	 * and remove the fractional part of the value.
	 *
	 * @return the raw value rounded to the closest {@code long} value.
	 * @see Math#round(double)
	 */
	public final long roundToLong() { return Math.round(getAsDouble()); }

	/** @return the hash code of the raw value. */
	@Override public final int hashCode() { return Double.hashCode(getAsDouble()); }

	/** @return true if the raw values are equal, and the objects are the same type. */
	@SuppressWarnings("unchecked")
	@Override public final boolean equals(final @Nullable Object obj)
	{
		return obj != null && getClass().equals(obj.getClass()) && eq((This) obj);
	}

	/**
	 * Compare to a non-null object of the same type.
	 * This implementation skips the null and type checks, so it may be faster than {@link #equals(Object)}.
	 *
	 * @param that a non-null instance of the same type.
	 * @return true if the objects are equal.
	 * @throws NullPointerException if {@code that} is null.
	 */
	public final boolean eq(final This that)
	{
		Assert.notNull(() -> that, "eq() does not support null");
		return this == that || this.getAsDouble() == that.getAsDouble();
	}

	/**
	 * Override this method to provide custom {@link Object#toString} formatting.
	 * The default passes the call through to {@link Double#toString()}.
	 */
	@SuppressWarnings("DesignForExtension")
	@Override public String toString()
	{
		return Double.toString(getAsDouble());
	}

	/** Compare the raw values. */
	@Override public final int compareTo(final This that)
	{
		return Double.compare(getAsDouble(), that.getAsDouble());
	}

	/**
	 * @param that a value to compare with.
	 * @return negative if {@code this} is less than {@code that}, positive if greater, or zero if equal.
	 */
	public final int compareTo(final double that)
	{
		return Double.compare(getAsDouble(), that);
	}

	/**
	 * @param that a supplier of the value to compare against.
	 * @return negative if {@code this} is less than {@code that}, positive if greater, or zero if equal.
	 */
	public final int compareTo(final DoubleSupplier that) { return compareTo(that.getAsDouble()); }
}
