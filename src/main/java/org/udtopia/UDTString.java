package org.udtopia;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.udtopia.assertion.Assert;

/**
 * A value type wrapping a {@link String}.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class UDTString<This extends UDTString<This>>
	implements UDTComparable<This>, Supplier<String>
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
}
