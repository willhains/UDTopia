package org.udtopia;

import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import javax.annotation.Nullable;
import org.udtopia.assertion.Assert;

/**
 * A value type wrapping a primitive {@code int}.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class UDTInt<This extends UDTInt<This>> implements UDTNumber<This>
{
	// The single-argument factory of the subclass
	private final IntFunction<This> _factory;

	/** @param factory a method reference to the factory of the implementing subclass. */
	protected UDTInt(final IntFunction<This> factory) { _factory = factory; }

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

	/**
	 * Wrap the raw value in another type.
	 *
	 * @param factory a constructor or factory method reference for the desired type.
	 * @param <Result> the return type.
	 * @return the output of the factory.
	 */
	public final <Result> Result getAs(final IntFunction<Result> factory)
	{
		return map(raw -> raw, factory);
	}

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

	/**
	 * Build a new value of this type with the raw underlying value converted by {@code mapper}.
	 *
	 * @param mapper the mapping function to apply to the raw underlying value.
	 * @return a new instance of this type.
	 */
	public final This map(final IntUnaryOperator mapper)
	{
		final int mapped = mapper.applyAsInt(getAsInt());
		if (mapped == getAsInt())
		{
			@SuppressWarnings("unchecked") final This self = (This) this;
			return self;
		}
		return _factory.apply(mapped);
	}

	/**
	 * Convert to another type by applying a mapping function to the raw value and passing to a {@code factory}.
	 *
	 * @param mapper the mapping function to apply to the raw underlying value.
	 * @param factory a constructor/factory of the desired result type.
	 * @param <Result> the resulting type.
	 * @return the result of the {@code factory} function.
	 */
	public final <Result> Result map(final IntUnaryOperator mapper, final IntFunction<? extends Result> factory)
	{
		return factory.apply(mapper.applyAsInt(getAsInt()));
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

	/**
	 * @param that the value to compare against.
	 * @return {@code true} if the raw value is greater than (but not equal to) {@code that}; {@code false} otherwise.
	 */
	public final boolean isGreaterThan(final int that) { return this.getAsInt() > that; }

	/**
	 * @param that a supplier of the value to compare against.
	 * @return {@code true} if the raw value is greater than (but not equal to) the value supplied by {@code that}.
	 */
	public final boolean isGreaterThan(final IntSupplier that) { return this.getAsInt() > that.getAsInt(); }

	/**
	 * @param that the value to compare against.
	 * @return {@code true} if the raw value is greater than or equal to {@code that}; {@code false} otherwise.
	 */
	public final boolean isGreaterThanOrEqualTo(final int that) { return this.getAsInt() >= that; }

	/**
	 * @param that a supplier of the value to compare against.
	 * @return {@code true} if the raw value is greater than or equal to the value supplied by {@code that}.
	 */
	public final boolean isGreaterThanOrEqualTo(final IntSupplier that) { return this.getAsInt() >= that.getAsInt(); }

	/**
	 * @param that the value to compare against.
	 * @return {@code true} if the raw value is less than (but not equal to) {@code that}; {@code false} otherwise.
	 */
	public final boolean isLessThan(final int that) { return this.getAsInt() < that; }

	/**
	 * @param that a supplier of the value to compare against.
	 * @return {@code true} if the raw value is less than (but not equal to) the value supplied by {@code that}.
	 */
	public final boolean isLessThan(final IntSupplier that) { return this.getAsInt() < that.getAsInt(); }

	/**
	 * @param that the value to compare against.
	 * @return {@code true} if the raw value is less than or equal to {@code that}; {@code false} otherwise.
	 */
	public final boolean isLessThanOrEqualTo(final int that) { return this.getAsInt() <= that; }

	/**
	 * @param that a supplier of the value to compare against.
	 * @return {@code true} if the raw value is less than or equal to the value supplied by {@code that}.
	 */
	public final boolean isLessThanOrEqualTo(final IntSupplier that) { return this.getAsInt() <= that.getAsInt(); }
}
