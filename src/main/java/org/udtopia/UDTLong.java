package org.udtopia;

import java.util.function.LongSupplier;
import javax.annotation.Nullable;
import org.udtopia.assertion.Assert;

/**
 * A value type wrapping a primitive {@code long}.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class UDTLong<This extends UDTLong<This>> implements UDTNumber<This>
{
	/**
	 * If the raw value can be exactly represented by an {@code int}, convert it.
	 *
	 * @return the raw value as an {@code int}.
	 * @throws ArithmeticException if the raw value has a fractional part, or is outside the range of {@code int}.
	 */
	@Override public final int getAsInt()
	{
		final long raw = getAsLong();
		final int rawAsInt = (int) raw;
		if (rawAsInt != raw) { throw new ArithmeticException("Cannot exactly represent as int: " + this); }
		return rawAsInt;
	}

	/** @return the raw value. */
	@Override public abstract long getAsLong();

	/** @return the raw value as a {@code double} value. */
	@Override public final double getAsDouble() { return getAsLong(); }

	/**
	 * Convert the raw value to an {@code int} without throwing.
	 * Unlike {@link #getAsInt()}, this method will <b>change the value</b> to fit within the range of {@code int}.
	 *
	 * @return the raw value rounded to the closest {@code int} value.
	 * @see UDTInt#nearestInt
	 */
	public final int roundToInt() { return UDTInt.nearestInt(getAsLong()); }

	/** @return the hash code of the raw value. */
	@Override public final int hashCode() { return Long.hashCode(getAsLong()); }

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
		return this == that || this.getAsLong() == that.getAsLong();
	}

	/**
	 * Override this method to provide custom {@link Object#toString} formatting.
	 * The default passes the call through to {@link Long#toString()}.
	 */
	@SuppressWarnings("DesignForExtension")
	@Override public String toString()
	{
		return Long.toString(getAsLong());
	}

	/** Compare the raw values. */
	@Override public final int compareTo(final This that)
	{
		return Long.compare(getAsLong(), that.getAsLong());
	}

	/**
	 * @param that a value to compare with.
	 * @return negative if {@code this} is less than {@code that}, positive if greater, or zero if equal.
	 */
	public final int compareTo(final long that)
	{
		return Long.compare(this.getAsLong(), that);
	}

	/**
	 * @param that a supplier of the value to compare against.
	 * @return negative if {@code this} is less than {@code that}, positive if greater, or zero if equal.
	 */
	public final int compareTo(final LongSupplier that) { return compareTo(that.getAsLong()); }
}
