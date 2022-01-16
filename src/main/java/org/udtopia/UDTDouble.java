package org.udtopia;

import java.text.NumberFormat;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;
import javax.annotation.Nullable;
import org.udtopia.assertion.Assert;
import org.udtopia.rules.DoubleRule;

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
	 * Parse a string value to build a UDT value.
	 *
	 * @param factory a method reference to the factory of the implementing subclass.
	 * @param string a string representation of the value.
	 * @param <This> the UDT type to return.
	 * @return a UDT value derived from the string value.
	 */
	protected static <This extends UDTDouble<This>> This parse(
		final DoubleFunction<This> factory,
		final String string)
	{
		return factory.apply(Double.parseDouble(string));
	}

	/**
	 * Apply the {@link DoubleRule}s annotated on the specified class.
	 *
	 * @param type the subclass.
	 * @param rawValue the raw value to apply the rules on.
	 * @return the resulting raw value to use.
	 */
	protected static double applyRules(final Class<?> type, final double rawValue)
	{
		return DoubleRule.applyRulesFor(type, rawValue);
	}

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

	@Override public final This negate() { return map(raw -> -raw); }

	@Override public final String format(final NumberFormat formatter) { return formatter.format(getAsDouble()); }

	/**
	 * Add a number to the raw value, and wrap the result in the same type.
	 *
	 * @param that the number to add.
	 * @return an instance of {@link This}, wrapping the addition result.
	 */
	public final This add(final double that) { return map(raw -> raw + that); }

	/**
	 * Add a number to the raw value, and wrap the result in the same type.
	 *
	 * @param that the number to add.
	 * @return an instance of {@link This}, wrapping the addition result.
	 */
	public final This add(final DoubleSupplier that) { return add(that.getAsDouble()); }

	/**
	 * Add a number to the raw value, and wrap the result in another type.
	 *
	 * @param that the number to add.
	 * @param factory a method reference to the factory/constructor of the return type.
	 * @param <Result> the return type.
	 * @return an instance of {@code Result}, wrapping the addition result.
	 */
	public final <Result> Result add(final DoubleSupplier that, final DoubleFunction<Result> factory)
	{
		return factory.apply(this.getAsDouble() + that.getAsDouble());
	}

	/**
	 * Subtract a number from the raw value, and wrap the result in the same type.
	 * Note: This is the reverse of {@link #subtractFrom(double)}.
	 *
	 * @param that the number to subtract.
	 * @return an instance of {@link This}, wrapping the subtraction result.
	 * @see #subtractFrom(double)
	 */
	public final This subtract(final double that) { return map(raw -> raw - that); }

	/**
	 * Subtract a number from the raw value, and wrap the result in the same type.
	 * Note: This is the reverse of {@link #subtractFrom(DoubleSupplier)}.
	 *
	 * @param that the number to subtract.
	 * @return an instance of {@link This}, wrapping the subtraction result.
	 * @see #subtractFrom(DoubleSupplier)
	 */
	public final This subtract(final DoubleSupplier that) { return subtract(that.getAsDouble()); }

	/**
	 * Subtract a number from the raw value, and wrap the result in another type.
	 * Note: This is the reverse of {@link #subtractFrom(DoubleSupplier, DoubleFunction)}.
	 *
	 * @param that the number to subtract.
	 * @param factory a method reference to the factory/constructor of the return type.
	 * @param <Result> the return type.
	 * @return an instance of {@code Result}, wrapping the subtraction result.
	 * @see #subtractFrom(DoubleSupplier, DoubleFunction)
	 */
	public final <Result> Result subtract(final DoubleSupplier that, final DoubleFunction<Result> factory)
	{
		return factory.apply(this.getAsDouble() - that.getAsDouble());
	}

	/**
	 * Subtract the raw value from a number, and wrap the result in the same type.
	 * Note: This is the reverse of {@link #subtract(double)}.
	 *
	 * @param that the number to subtract.
	 * @return an instance of {@link This}, wrapping the subtraction result.
	 * @see #subtract(double)
	 */
	public final This subtractFrom(final double that) { return map(raw -> that - raw); }

	/**
	 * Subtract the raw value from a number, and wrap the result in the same type.
	 * Note: This is the reverse of {@link #subtract(DoubleSupplier)}.
	 *
	 * @param that the number to subtract.
	 * @return an instance of {@link This}, wrapping the subtraction result.
	 * @see #subtract(DoubleSupplier)
	 */
	public final This subtractFrom(final DoubleSupplier that) { return subtractFrom(that.getAsDouble()); }

	/**
	 * Subtract the raw value from a number, and wrap the result in another type.
	 * Note: This is the reverse of {@link #subtract(DoubleSupplier, DoubleFunction)}.
	 *
	 * @param that the number to subtract.
	 * @param factory a method reference to the factory/constructor of the return type.
	 * @param <Result> the return type.
	 * @return an instance of {@code Result}, wrapping the subtraction result.
	 * @see #subtract(DoubleSupplier, DoubleFunction)
	 */
	public final <Result> Result subtractFrom(final DoubleSupplier that, final DoubleFunction<Result> factory)
	{
		return factory.apply(that.getAsDouble() - this.getAsDouble());
	}

	/**
	 * Multiply the raw value by a number, and wrap the result in the same type.
	 *
	 * @param that the number to multiply by.
	 * @return an instance of {@link This}, wrapping the multiplication result.
	 */
	public final This multiplyBy(final double that) { return map(raw -> raw * that); }

	/**
	 * Multiply the raw value by a number, and wrap the result in the same type.
	 *
	 * @param that the number to multiply by.
	 * @return an instance of {@link This}, wrapping the multiplication result.
	 */
	public final This multiplyBy(final DoubleSupplier that) { return multiplyBy(that.getAsDouble()); }

	/**
	 * Multiply the raw value by a number, and wrap the result in another type.
	 *
	 * @param that the number to multiply by.
	 * @param factory a method reference to the factory/constructor of the return type.
	 * @param <Result> the return type.
	 * @return an instance of {@code Result}, wrapping the multiplication result.
	 */
	public final <Result> Result multiplyBy(final DoubleSupplier that, final DoubleFunction<Result> factory)
	{
		return factory.apply(this.getAsDouble() * that.getAsDouble());
	}

	/**
	 * Divide the raw value by a number, and wrap the result in the same type.
	 * Note: This is the inverse of {@link #divide(double)}.
	 *
	 * @param that the number to divide by.
	 * @return an instance of {@link This}, wrapping the division result.
	 */
	public final This divideBy(final double that) { return map(raw -> raw / that); }

	/**
	 * Divide the raw value by a number, and wrap the result in the same type.
	 * Note: This is the inverse of {@link #divide(DoubleSupplier)}.
	 *
	 * @param that the number to divide by.
	 * @return an instance of {@link This}, wrapping the division result.
	 */
	public final This divideBy(final DoubleSupplier that) { return divideBy(that.getAsDouble()); }

	/**
	 * Divide the raw value by a number, and wrap the result in another type.
	 * Note: This is the inverse of {@link #divide(DoubleSupplier, DoubleFunction)}.
	 *
	 * @param that the number to divide by.
	 * @param factory a method reference to the factory/constructor of the return type.
	 * @param <Result> the return type.
	 * @return an instance of {@code Result}, wrapping the multiplication result.
	 */
	public final <Result> Result divideBy(final DoubleSupplier that, final DoubleFunction<Result> factory)
	{
		return factory.apply(this.getAsDouble() / that.getAsDouble());
	}

	/**
	 * Divide a number by the raw value, and wrap the result in the same type.
	 * Note: This is the inverse of {@link #divideBy(double)}.
	 *
	 * @param that the number to divide.
	 * @return an instance of {@link This}, wrapping the division result.
	 */
	public final This divide(final double that) { return map(raw -> that / raw); }

	/**
	 * Divide a number by the raw value, and wrap the result in the same type.
	 * Note: This is the inverse of {@link #divideBy(DoubleSupplier)}.
	 *
	 * @param that the number to divide.
	 * @return an instance of {@link This}, wrapping the division result.
	 */
	public final This divide(final DoubleSupplier that) { return divide(that.getAsDouble()); }

	/**
	 * Divide a number by the raw value, and wrap the result in another type.
	 * Note: This is the inverse of {@link #divideBy(DoubleSupplier, DoubleFunction)}.
	 *
	 * @param that the number to divide.
	 * @param factory a method reference to the factory/constructor of the return type.
	 * @param <Result> the return type.
	 * @return an instance of {@code Result}, wrapping the multiplication result.
	 */
	public final <Result> Result divide(final DoubleSupplier that, final DoubleFunction<Result> factory)
	{
		return factory.apply(that.getAsDouble() / this.getAsDouble());
	}

	/** @return the inverse value. */
	public final This invert() { return map(raw -> 1.0 / raw); }

	/**
	 * Round the raw value to the nearest integer, and wrap the result in the same type.
	 *
	 * @return an instance of {@link This}, wrapping the rounded result.
	 */
	public final This round() { return map(Math::round); }

	/**
	 * Round the raw value up to the next integer, and wrap the result in the same type.
	 *
	 * @return an instance of {@link This}, wrapping the rounded result.
	 */
	public final This roundUp() { return map(Math::ceil); }

	/**
	 * Round the raw value down to the next integer, and wrap the result in the same type.
	 *
	 * @return an instance of {@link This}, wrapping the rounded result.
	 */
	public final This roundDown() { return map(Math::floor); }
}
