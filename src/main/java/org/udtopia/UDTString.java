package org.udtopia;

import java.util.function.Supplier;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import org.udtopia.assertion.Assert;

/**
 * A value type wrapping a {@link String}.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class UDTString<This extends UDTString<This>>
	implements UDTComparable<This>, CharSequence, Supplier<String>
{
	/** @return the raw value. */
	@Override public abstract String get();

	/** @return the hash code of the raw value. */
	@Override public final int hashCode() { return get().hashCode(); }

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
		return this == that || this.get().equals(that.get());
	}

	/**
	 * Override this method to provide custom {@link Object#toString} formatting.
	 * The default returns the raw string value.
	 */
	@SuppressWarnings("DesignForExtension")
	@Override public String toString() { return get(); }

	@Override public final IntStream chars() { return get().chars(); }

	@Override public final IntStream codePoints() { return get().codePoints(); }

	@Override public final int length() { return get().length(); }

	/** @return {@code true} if the raw string value is zero-length. */
	public final boolean isEmpty() { return get().isEmpty(); }

	@Override public final char charAt(final int index) { return get().charAt(index); }

	@Override public final This subSequence(final int start, final int end)
	{
		return map(raw -> raw.subSequence(start, end).toString());
	}

	/** Compare the raw values. */
	@Override public final int compareTo(final This that)
	{
		return get().compareTo(that.get());
	}

	/**
	 * @param that a value to compare with.
	 * @return negative if {@code this} is less than {@code that}, positive if greater, or zero if equal.
	 */
	public final int compareTo(final String that)
	{
		return this.get().compareTo(that);
	}

	/**
	 * @param that a supplier of the value to compare against.
	 * @return negative if {@code this} is less than {@code that}, positive if greater, or zero if equal.
	 */
	public final int compareTo(final Supplier<String> that) { return compareTo(that.get()); }

	/**
	 * @param that the value to compare against.
	 * @return {@code true} if the raw value is greater than (but not equal to) {@code that}; {@code false} otherwise.
	 */
	public final boolean isGreaterThan(final String that) { return this.get().compareTo(that) > 0; }

	/**
	 * @param that a supplier of the value to compare against.
	 * @return {@code true} if the raw value is greater than (but not equal to) the value supplied by {@code that}.
	 */
	public final boolean isGreaterThan(final Supplier<String> that) { return this.get().compareTo(that.get()) > 0; }

	/**
	 * @param that the value to compare against.
	 * @return {@code true} if the raw value is greater than or equal to {@code that}; {@code false} otherwise.
	 */
	public final boolean isGreaterThanOrEqualTo(final String that) { return this.get().compareTo(that) >= 0; }

	/**
	 * @param that a supplier of the value to compare against.
	 * @return {@code true} if the raw value is greater than or equal to {@code that}; {@code false} otherwise.
	 */
	public final boolean isGreaterThanOrEqualTo(final Supplier<String> that)
	{
		return this.get().compareTo(that.get()) >= 0;
	}

	/**
	 * @param that the value to compare against.
	 * @return {@code true} if the raw value is less than (but not equal to) {@code that}; {@code false} otherwise.
	 */
	public final boolean isLessThan(final String that) { return this.get().compareTo(that) < 0; }

	/**
	 * @param that a supplier of the value to compare against.
	 * @return {@code true} if the raw value is less than (but not equal to) {@code that}; {@code false} otherwise.
	 */
	public final boolean isLessThan(final Supplier<String> that) { return this.get().compareTo(that.get()) < 0; }

	/**
	 * @param that the value to compare against.
	 * @return {@code true} if the raw value is less than or equal to {@code that}; {@code false} otherwise.
	 */
	public final boolean isLessThanOrEqualTo(final String that) { return this.get().compareTo(that) <= 0; }

	/**
	 * @param that a supplier of the value to compare against.
	 * @return {@code true} if the raw value is less than or equal to {@code that}; {@code false} otherwise.
	 */
	public final boolean isLessThanOrEqualTo(final Supplier<String> that)
	{
		return this.get().compareTo(that.get()) <= 0;
	}
}
