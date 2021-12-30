package org.udtopia;

import java.text.NumberFormat;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import javax.annotation.Nullable;
import org.udtopia.assertion.Assert;

import static java.lang.Math.*;

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

	/**
	 * Test the raw value with {@code condition}.
	 *
	 * @param condition a {@link IntPredicate} that tests the raw value.
	 * @return {@code true} if the underlying raw value satisfies {@code condition}; {@code false} otherwise.
	 */
	public final boolean is(final IntPredicate condition) { return condition.test(getAsInt()); }

	/**
	 * Reverse of {@link #is(IntPredicate)}.
	 *
	 * @param condition a {@link IntPredicate} that tests the raw value.
	 * @return {@code false} if the underlying raw value satisfies {@code condition}; {@code true} otherwise.
	 */
	public final boolean isNot(final IntPredicate condition) { return !is(condition); }

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

	@Override public final boolean isZero() { return getAsInt() == 0; }

	@Override public final boolean isPositive() { return getAsInt() > 0; }

	@Override public final boolean isNegative() { return getAsInt() < 0; }

	@Override public final This negate() { return map(Math::negateExact); }

	@Override public final String format(final NumberFormat formatter) { return formatter.format(getAsInt()); }

	/**
	 * Add a number to the raw value, and wrap the result in the same type.
	 *
	 * @param that the number to add.
	 * @return an instance of {@link This}, wrapping the addition result.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final This add(final int that) { return map(raw -> addExact(raw, that)); }

	/**
	 * Add a number to the raw value, and wrap the result in the same type.
	 *
	 * @param that the number to add.
	 * @return an instance of {@link This}, wrapping the addition result.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final This add(final IntSupplier that) { return add(that.getAsInt()); }

	/**
	 * Add a number to the raw value, and wrap the result in another type.
	 *
	 * @param that the number to add.
	 * @param factory a method reference to the factory/constructor of the return type.
	 * @param <Result> the return type.
	 * @return an instance of {@code Result}, wrapping the addition result.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final <Result> Result add(final IntSupplier that, final IntFunction<Result> factory)
	{
		return factory.apply(addExact(this.getAsInt(), that.getAsInt()));
	}

	/**
	 * Subtract a number from the raw value, and wrap the result in the same type.
	 *
	 * @param that the number to subtract.
	 * @return an instance of {@link This}, wrapping the subtraction result.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final This subtract(final int that) { return map(raw -> subtractExact(raw, that)); }

	/**
	 * Subtract a number from the raw value, and wrap the result in the same type.
	 *
	 * @param that the number to subtract.
	 * @return an instance of {@link This}, wrapping the subtraction result.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final This subtract(final IntSupplier that) { return subtract(that.getAsInt()); }

	/**
	 * Subtract a number from the raw value, and wrap the result in another type.
	 *
	 * @param that the number to subtract.
	 * @param factory a method reference to the factory/constructor of the return type.
	 * @param <Result> the return type.
	 * @return an instance of {@code Result}, wrapping the subtraction result.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final <Result> Result subtract(final IntSupplier that, final IntFunction<Result> factory)
	{
		return factory.apply(subtractExact(this.getAsInt(), that.getAsInt()));
	}

	/**
	 * Subtract the raw value from a number, and wrap the result in the same type.
	 * Note: This is the reverse of {@link #subtract(int)}.
	 *
	 * @param that the number to subtract.
	 * @return an instance of {@link This}, wrapping the subtraction result.
	 * @see #subtract(int)
	 */
	public final This subtractFrom(final int that) { return map(raw -> that - raw); }

	/**
	 * Subtract the raw value from a number, and wrap the result in the same type.
	 * Note: This is the reverse of {@link #subtract(IntSupplier)}.
	 *
	 * @param that the number to subtract.
	 * @return an instance of {@link This}, wrapping the subtraction result.
	 * @see #subtract(IntSupplier)
	 */
	public final This subtractFrom(final IntSupplier that) { return subtractFrom(that.getAsInt()); }

	/**
	 * Subtract the raw value from a number, and wrap the result in another type.
	 * Note: This is the reverse of {@link #subtract(IntSupplier, IntFunction)}.
	 *
	 * @param that the number to subtract.
	 * @param factory a method reference to the factory/constructor of the return type.
	 * @param <Result> the return type.
	 * @return an instance of {@code Result}, wrapping the subtraction result.
	 * @see #subtract(IntSupplier, IntFunction)
	 */
	public final <Result> Result subtractFrom(final IntSupplier that, final IntFunction<Result> factory)
	{
		return factory.apply(that.getAsInt() - this.getAsInt());
	}

	/**
	 * Multiply the raw value by a number, and wrap the result in the same type.
	 *
	 * @param that the number to multiply by.
	 * @return an instance of {@link This}, wrapping the multiplication result.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final This multiplyBy(final int that) { return map(raw -> multiplyExact(raw, that)); }

	/**
	 * Multiply the raw value by a number, and wrap the result in the same type.
	 *
	 * @param that the number to multiply by.
	 * @return an instance of {@link This}, wrapping the multiplication result.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final This multiplyBy(final IntSupplier that) { return multiplyBy(that.getAsInt()); }

	/**
	 * Multiply the raw value by a number, and wrap the result in another type.
	 *
	 * @param that the number to multiply by.
	 * @param factory a method reference to the factory/constructor of the return type.
	 * @param <Result> the return type.
	 * @return an instance of {@code Result}, wrapping the multiplication result.
	 * @throws ArithmeticException if the value overflows.
	 */
	public final <Result> Result multiplyBy(final IntSupplier that, final IntFunction<Result> factory)
	{
		return factory.apply(multiplyExact(this.getAsInt(), that.getAsInt()));
	}

	/**
	 * Divide the raw value by a number, and wrap the result in the same type.
	 *
	 * @param that the number to divide by.
	 * @return an instance of {@link This}, wrapping the division result.
	 */
	public final This divideBy(final int that) { return map(raw -> raw / that); }

	/**
	 * Divide the raw value by a number, and wrap the result in the same type.
	 *
	 * @param that the number to divide by.
	 * @return an instance of {@link This}, wrapping the division result.
	 */
	public final This divideBy(final IntSupplier that) { return divideBy(that.getAsInt()); }

	/**
	 * Divide the raw value by a number, and wrap the result in another type.
	 *
	 * @param that the number to divide by.
	 * @param factory a method reference to the factory/constructor of the return type.
	 * @param <Result> the return type.
	 * @return an instance of {@code Result}, wrapping the multiplication result.
	 */
	public final <Result> Result divideBy(final IntSupplier that, final IntFunction<Result> factory)
	{
		return factory.apply(this.getAsInt() / that.getAsInt());
	}

	/**
	 * Divide a number by the raw value, and wrap the result in the same type.
	 * Note: This is the inverse of {@link #divideBy(int)}.
	 *
	 * @param that the number to divide.
	 * @return an instance of {@link This}, wrapping the division result.
	 */
	public final This divide(final int that) { return map(raw -> that / raw); }

	/**
	 * Divide a number by the raw value, and wrap the result in the same type.
	 * Note: This is the inverse of {@link #divideBy(IntSupplier)}.
	 *
	 * @param that the number to divide.
	 * @return an instance of {@link This}, wrapping the division result.
	 */
	public final This divide(final IntSupplier that) { return divide(that.getAsInt()); }

	/**
	 * Divide a number by the raw value, and wrap the result in another type.
	 * Note: This is the inverse of {@link #divideBy(IntSupplier, IntFunction)}.
	 *
	 * @param that the number to divide.
	 * @param factory a method reference to the factory/constructor of the return type.
	 * @param <Result> the return type.
	 * @return an instance of {@code Result}, wrapping the multiplication result.
	 */
	public final <Result> Result divide(final IntSupplier that, final IntFunction<Result> factory)
	{
		return factory.apply(that.getAsInt() / this.getAsInt());
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
