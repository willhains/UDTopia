package org.udtopia;

import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.udtopia.assertion.Assert;

/**
 * A value type wrapping an underlying data type.
 *
 * @param <Raw> the underlying type.
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class UDTValue<Raw, This extends UDTValue<Raw, This>> implements Supplier<Raw>
{
	// A function to make defensive copies of the raw value
	private final Function<? super Raw, ? extends Raw> _defensiveCopier;

	/**
	 * @param defensiveCopier a function to make deep, defensive copies of the raw value.
	 */
	protected UDTValue(
		final Function<? super Raw, ? extends Raw> defensiveCopier)
	{
		_defensiveCopier = defensiveCopier;
	}

	/** @return raw value without a defensive copy. */
	protected abstract Raw rawWithoutDefensiveCopy();

	/** @return the raw value. */
	@Override public final Raw get() { return _defensiveCopier.apply(rawWithoutDefensiveCopy()); }

	/** @return the hash code of the raw value. */
	@Override public final int hashCode() { return rawWithoutDefensiveCopy().hashCode(); }

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
		return this == that || this.rawWithoutDefensiveCopy().equals(that.rawWithoutDefensiveCopy());
	}

	/**
	 * Override this method to provide custom {@link Object#toString} formatting.
	 * The default passes the call through to {@link Raw Raw.toString()}.
	 */
	@SuppressWarnings("DesignForExtension")
	@Override public String toString()
	{
		return rawWithoutDefensiveCopy().toString();
	}
}
