package org.udtopia;

import java.text.NumberFormat;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongUnaryOperator;
import javax.annotation.Nullable;
import org.udtopia.assertion.Assert;

import static java.lang.Math.*;

/**
 * A value type wrapping a primitive {@code long}.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class UDTLong<This extends UDTLong<This>> implements UDTNumber<This>
{
	// The single-argument factory of the subclass
	private final LongFunction<This> _factory;

	/** @param factory a method reference to the factory of the implementing subclass. */
	protected UDTLong(final LongFunction<This> factory) { _factory = factory; }

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
	 * Wrap the raw value in another type.
	 *
	 * @param factory a constructor or factory method reference for the desired type.
	 * @param <Result> the return type.
	 * @return the output of the factory.
	 */
	public final <Result> Result getAs(final LongFunction<Result> factory)
	{
		return map(raw -> raw, factory);
	}

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

	/**
	 * Build a new value of this type with the raw underlying value converted by {@code mapper}.
	 *
	 * @param mapper the mapping function to apply to the raw underlying value.
	 * @return a new instance of this type.
	 */
	public final This map(final LongUnaryOperator mapper)
	{
		final long mapped = mapper.applyAsLong(getAsLong());
		if (mapped == getAsLong())
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
	public final <Result> Result map(final LongUnaryOperator mapper, final LongFunction<? extends Result> factory)
	{
		return factory.apply(mapper.applyAsLong(getAsLong()));
	}

	/**
	 * Test the raw value with {@code condition}.
	 *
	 * @param condition a {@link LongPredicate} that tests the raw value.
	 * @return {@code true} if the underlying raw value satisfies {@code condition}; {@code false} otherwise.
	 */
	public final boolean is(final LongPredicate condition) { return condition.test(getAsLong()); }

	/**
	 * Reverse of {@link #is(LongPredicate)}.
	 *
	 * @param condition a {@link LongPredicate} that tests the raw value.
	 * @return {@code false} if the underlying raw value satisfies {@code condition}; {@code true} otherwise.
	 */
	public final boolean isNot(final LongPredicate condition) { return !is(condition); }

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

	/**
	 * @param that the value to compare against.
	 * @return {@code true} if the raw value is greater than (but not equal to) {@code that}; {@code false} otherwise.
	 */
	public final boolean isGreaterThan(final long that) { return this.getAsLong() > that; }

	/**
	 * @param that a supplier of the value to compare against.
	 * @return {@code true} if the raw value is greater than (but not equal to) the value supplied by {@code that}.
	 */
	public final boolean isGreaterThan(final LongSupplier that) { return this.getAsLong() > that.getAsLong(); }

	/**
	 * @param that the value to compare against.
	 * @return {@code true} if the raw value is greater than or equal to {@code that}; {@code false} otherwise.
	 */
	public final boolean isGreaterThanOrEqualTo(final long that) { return this.getAsLong() >= that; }

	/**
	 * @param that a supplier of the value to compare against.
	 * @return {@code true} if the raw value is greater than or equal to the value supplied by {@code that}.
	 */
	public final boolean isGreaterThanOrEqualTo(final LongSupplier that)
	{
		return this.getAsLong() >= that.getAsLong();
	}

	/**
	 * @param that the value to compare against.
	 * @return {@code true} if the raw value is less than (but not equal to) {@code that}; {@code false} otherwise.
	 */
	public final boolean isLessThan(final long that) { return this.getAsLong() < that; }

	/**
	 * @param that a supplier of the value to compare against.
	 * @return {@code true} if the raw value is less than (but not equal to) the value supplied by {@code that}.
	 */
	public final boolean isLessThan(final LongSupplier that) { return this.getAsLong() < that.getAsLong(); }

	/**
	 * @param that the value to compare against.
	 * @return {@code true} if the raw value is less than or equal to {@code that}; {@code false} otherwise.
	 */
	public final boolean isLessThanOrEqualTo(final long that) { return this.getAsLong() <= that; }

	/**
	 * @param that a supplier of the value to compare against.
	 * @return {@code true} if the raw value is less than or equal to the value supplied by {@code that}.
	 */
	public final boolean isLessThanOrEqualTo(final LongSupplier that) { return this.getAsLong() <= that.getAsLong(); }

	@Override public final boolean isZero() { return getAsLong() == 0L; }

	@Override public final boolean isPositive() { return getAsLong() > 0L; }

	@Override public final boolean isNegative() { return getAsLong() < 0L; }

	@Override public final This negate() { return map(Math::negateExact); }

	@Override public final String format(final NumberFormat formatter) { return formatter.format(getAsLong()); }

	/**
	 * Add a number to the raw value, and wrap the result in the same type.
	 *
	 * @param that the number to add.
	 * @return an instance of {@link This}, wrapping the addition result.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final This add(final long that) { return map(raw -> addExact(raw, that)); }

	/**
	 * Add a number to the raw value, and wrap the result in the same type.
	 *
	 * @param that the number to add.
	 * @return an instance of {@link This}, wrapping the addition result.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final This add(final LongSupplier that) { return add(that.getAsLong()); }

	/**
	 * Add a number to the raw value, and wrap the result in another type.
	 *
	 * @param that the number to add.
	 * @param factory a method reference to the factory/constructor of the return type.
	 * @param <Result> the return type.
	 * @return an instance of {@code Result}, wrapping the addition result.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final <Result> Result add(final LongSupplier that, final LongFunction<Result> factory)
	{
		return factory.apply(addExact(this.getAsLong(), that.getAsLong()));
	}

	/**
	 * Subtract a number from the raw value, and wrap the result in the same type.
	 *
	 * @param that the number to subtract.
	 * @return an instance of {@link This}, wrapping the subtraction result.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final This subtract(final long that) { return map(raw -> subtractExact(raw, that)); }

	/**
	 * Subtract a number from the raw value, and wrap the result in the same type.
	 *
	 * @param that the number to subtract.
	 * @return an instance of {@link This}, wrapping the subtraction result.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final This subtract(final LongSupplier that) { return subtract(that.getAsLong()); }

	/**
	 * Subtract a number from the raw value, and wrap the result in another type.
	 *
	 * @param that the number to subtract.
	 * @param factory a method reference to the factory/constructor of the return type.
	 * @param <Result> the return type.
	 * @return an instance of {@code Result}, wrapping the subtraction result.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final <Result> Result subtract(final LongSupplier that, final LongFunction<Result> factory)
	{
		return factory.apply(subtractExact(this.getAsLong(), that.getAsLong()));
	}

	/**
	 * Subtract the raw value from a number, and wrap the result in the same type.
	 * Note: This is the reverse of {@link #subtract(long)}.
	 *
	 * @param that the number to subtract.
	 * @return an instance of {@link This}, wrapping the subtraction result.
	 * @see #subtract(long)
	 */
	public final This subtractFrom(final long that) { return map(raw -> that - raw); }

	/**
	 * Subtract the raw value from a number, and wrap the result in the same type.
	 * Note: This is the reverse of {@link #subtract(LongSupplier)}.
	 *
	 * @param that the number to subtract.
	 * @return an instance of {@link This}, wrapping the subtraction result.
	 * @see #subtract(LongSupplier)
	 */
	public final This subtractFrom(final LongSupplier that) { return subtractFrom(that.getAsLong()); }

	/**
	 * Subtract the raw value from a number, and wrap the result in another type.
	 * Note: This is the reverse of {@link #subtract(LongSupplier, LongFunction)}.
	 *
	 * @param that the number to subtract.
	 * @param factory a method reference to the factory/constructor of the return type.
	 * @param <Result> the return type.
	 * @return an instance of {@code Result}, wrapping the subtraction result.
	 * @see #subtract(LongSupplier, LongFunction)
	 */
	public final <Result> Result subtractFrom(final LongSupplier that, final LongFunction<Result> factory)
	{
		return factory.apply(that.getAsLong() - this.getAsLong());
	}

	/**
	 * Multiply the raw value by a number, and wrap the result in the same type.
	 *
	 * @param that the number to multiply by.
	 * @return an instance of {@link This}, wrapping the multiplication result.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final This multiplyBy(final long that) { return map(raw -> multiplyExact(raw, that)); }

	/**
	 * Multiply the raw value by a number, and wrap the result in the same type.
	 *
	 * @param that the number to multiply by.
	 * @return an instance of {@link This}, wrapping the multiplication result.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final This multiplyBy(final LongSupplier that) { return multiplyBy(that.getAsLong()); }

	/**
	 * Multiply the raw value by a number, and wrap the result in another type.
	 *
	 * @param that the number to multiply by.
	 * @param factory a method reference to the factory/constructor of the return type.
	 * @param <Result> the return type.
	 * @return an instance of {@code Result}, wrapping the multiplication result.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final <Result> Result multiplyBy(final LongSupplier that, final LongFunction<Result> factory)
	{
		return factory.apply(multiplyExact(this.getAsLong(), that.getAsLong()));
	}

	/**
	 * Divide the raw value by a number, and wrap the result in the same type.
	 *
	 * @param that the number to divide by.
	 * @return an instance of {@link This}, wrapping the division result.
	 */
	public final This divideBy(final long that) { return map(raw -> raw / that); }

	/**
	 * Divide the raw value by a number, and wrap the result in the same type.
	 *
	 * @param that the number to divide by.
	 * @return an instance of {@link This}, wrapping the division result.
	 */
	public final This divideBy(final LongSupplier that) { return divideBy(that.getAsLong()); }

	/**
	 * Divide the raw value by a number, and wrap the result in another type.
	 *
	 * @param that the number to divide by.
	 * @param factory a method reference to the factory/constructor of the return type.
	 * @param <Result> the return type.
	 * @return an instance of {@code Result}, wrapping the multiplication result.
	 */
	public final <Result> Result divideBy(final LongSupplier that, final LongFunction<Result> factory)
	{
		return factory.apply(this.getAsLong() / that.getAsLong());
	}

	/**
	 * Divide a number by the raw value, and wrap the result in the same type.
	 * Note: This is the inverse of {@link #divideBy(long)}.
	 *
	 * @param that the number to divide.
	 * @return an instance of {@link This}, wrapping the division result.
	 */
	public final This divide(final long that) { return map(raw -> that / raw); }

	/**
	 * Divide a number by the raw value, and wrap the result in the same type.
	 * Note: This is the inverse of {@link #divideBy(LongSupplier)}.
	 *
	 * @param that the number to divide.
	 * @return an instance of {@link This}, wrapping the division result.
	 */
	public final This divide(final LongSupplier that) { return divide(that.getAsLong()); }

	/**
	 * Divide a number by the raw value, and wrap the result in another type.
	 * Note: This is the inverse of {@link #divideBy(LongSupplier, LongFunction)}.
	 *
	 * @param that the number to divide.
	 * @param factory a method reference to the factory/constructor of the return type.
	 * @param <Result> the return type.
	 * @return an instance of {@code Result}, wrapping the multiplication result.
	 */
	public final <Result> Result divide(final LongSupplier that, final LongFunction<Result> factory)
	{
		return factory.apply(that.getAsLong() / this.getAsLong());
	}

	/**
	 * @return the raw value plus one.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final This increment() { return map(Math::incrementExact); }

	/**
	 * @return the raw value minus one.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final This decrement() { return map(Math::decrementExact); }
}
