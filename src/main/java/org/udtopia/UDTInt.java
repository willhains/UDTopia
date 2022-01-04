package org.udtopia;

import java.util.function.IntSupplier;
import javax.annotation.Nullable;
import org.udtopia.assertion.Assert;

/**
 * A value type wrapping a primitive {@code int}.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class UDTInt<This extends UDTInt<This>> implements UDTNumber<This>
{
	/**
	 * Convert a {@code long} to an {@code int} without throwing.
	 * This method will <b>change the value</b> to fit within the range of {@code int}, without changing the sign.
	 *
	 * @param longValue the value to round.
	 * @return the nearest {@code int} value.
	 */
	public static int nearestInt(final long longValue)
	{
		return (int) Math.min(Integer.MAX_VALUE, Math.max(Integer.MIN_VALUE, longValue));
	}

	/** @return the raw value. */
	@Override public abstract int getAsInt();

	/** @return the raw value as a {@code long} value. */
	@Override public final long getAsLong() { return getAsInt(); }

	/** @return the raw value as a {@code double} value. */
	@Override public final double getAsDouble() { return getAsInt(); }

	/** @return the hash code of the raw value. */
	@Override public final int hashCode() { return Integer.hashCode(getAsInt()); }

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
		return this == that || this.getAsInt() == that.getAsInt();
	}

	/**
	 * Override this method to provide custom {@link Object#toString} formatting.
	 * The default passes the call through to {@link Integer#toString()}.
	 */
	@SuppressWarnings("DesignForExtension")
	@Override public String toString()
	{
		return Integer.toString(getAsInt());
	}

	/** Compare the raw values. */
	@Override public final int compareTo(final This that)
	{
		return Integer.compare(getAsInt(), that.getAsInt());
	}

	/**
	 * @param that a value to compare with.
	 * @return negative if {@code this} is less than {@code that}, positive if greater, or zero if equal.
	 */
	public final int compareTo(final int that)
	{
		return Integer.compare(this.getAsInt(), that);
	}

	/**
	 * @param that a supplier of the value to compare against.
	 * @return negative if {@code this} is less than {@code that}, positive if greater, or zero if equal.
	 */
	public final int compareTo(final IntSupplier that) { return compareTo(that.getAsInt()); }
}
