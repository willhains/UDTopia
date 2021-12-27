package org.udtopia;

import java.text.NumberFormat;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;
import javax.annotation.Nullable;
import org.udtopia.assertion.Assert;

/**
 * A value type wrapping a primitive {@code double}.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class UDTDouble<This extends UDTDouble<This>> implements UDTNumber<This>
{
	// The single-argument factory of the subclass
	private final DoubleFunction<This> _factory;

	/** @param factory a method reference to the factory of the implementing subclass. */
	protected UDTDouble(final DoubleFunction<This> factory) { _factory = factory; }

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
	 * Wrap the raw value in another type.
	 *
	 * @param factory a constructor or factory method reference for the desired type.
	 * @param <Result> the return type.
	 * @return the output of the factory.
	 */
	public final <Result> Result getAs(final DoubleFunction<Result> factory)
	{
		return map(raw -> raw, factory);
	}

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

	/**
	 * Build a new value of this type with the raw underlying value converted by {@code mapper}.
	 *
	 * @param mapper the mapping function to apply to the raw underlying value.
	 * @return a new instance of this type.
	 */
	public final This map(final DoubleUnaryOperator mapper)
	{
		final double mapped = mapper.applyAsDouble(getAsDouble());
		if (mapped == getAsDouble())
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
	public final <Result> Result map(final DoubleUnaryOperator mapper, final DoubleFunction<? extends Result> factory)
	{
		return factory.apply(mapper.applyAsDouble(getAsDouble()));
	}

	/**
	 * Test the raw value with {@code condition}.
	 *
	 * @param condition a {@link DoublePredicate} that tests the raw value.
	 * @return {@code true} if the underlying raw value satisfies {@code condition}; {@code false} otherwise.
	 */
	public final boolean is(final DoublePredicate condition) { return condition.test(getAsDouble()); }

	/**
	 * Reverse of {@link #is(DoublePredicate)}.
	 *
	 * @param condition a {@link DoublePredicate} that tests the raw value.
	 * @return {@code false} if the underlying raw value satisfies {@code condition}; {@code true} otherwise.
	 */
	public final boolean isNot(final DoublePredicate condition) { return !is(condition); }

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

	/**
	 * @param that the value to compare against.
	 * @return {@code true} if the raw value is greater than (but not equal to) {@code that}; {@code false} otherwise.
	 */
	public final boolean isGreaterThan(final double that) { return this.getAsDouble() > that; }

	/**
	 * @param that a supplier of the value to compare against.
	 * @return {@code true} if the raw value is greater than (but not equal to) the value supplied by {@code that}.
	 */
	public final boolean isGreaterThan(final DoubleSupplier that) { return this.getAsDouble() > that.getAsDouble(); }

	/**
	 * @param that the value to compare against.
	 * @return {@code true} if the raw value is greater than or equal to {@code that}; {@code false} otherwise.
	 */
	public final boolean isGreaterThanOrEqualTo(final double that) { return this.getAsDouble() >= that; }

	/**
	 * @param that a supplier of the value to compare against.
	 * @return {@code true} if the raw value is greater than or equal to the value supplied by {@code that}.
	 */
	public final boolean isGreaterThanOrEqualTo(final DoubleSupplier that)
	{
		return this.getAsDouble() >= that.getAsDouble();
	}

	/**
	 * @param that the value to compare against.
	 * @return {@code true} if the raw value is less than (but not equal to) {@code that}; {@code false} otherwise.
	 */
	public final boolean isLessThan(final double that) { return this.getAsDouble() < that; }

	/**
	 * @param that a supplier of the value to compare against.
	 * @return {@code true} if the raw value is less than (but not equal to) the value supplied by {@code that}.
	 */
	public final boolean isLessThan(final DoubleSupplier that) { return this.getAsDouble() < that.getAsDouble(); }

	/**
	 * @param that the value to compare against.
	 * @return {@code true} if the raw value is less than or equal to {@code that}; {@code false} otherwise.
	 */
	public final boolean isLessThanOrEqualTo(final double that) { return this.getAsDouble() <= that; }

	/**
	 * @param that a supplier of the value to compare against.
	 * @return {@code true} if the raw value is less than or equal to the value supplied by {@code that}.
	 */
	public final boolean isLessThanOrEqualTo(final DoubleSupplier that)
	{
		return this.getAsDouble() <= that.getAsDouble();
	}

	@Override public final boolean isZero() { return getAsDouble() == 0.0; }

	@Override public final boolean isPositive() { return getAsDouble() > 0.0; }

	@Override public final boolean isNegative() { return getAsDouble() < 0.0; }

	@Override public final String format(final NumberFormat formatter) { return formatter.format(getAsDouble()); }
}
