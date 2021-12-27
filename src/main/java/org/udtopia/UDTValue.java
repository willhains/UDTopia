package org.udtopia;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import javax.annotation.Nullable;
import org.udtopia.assertion.Assert;

/**
 * A value type wrapping an underlying data type.
 *
 * @param <Raw> the underlying type. Not an array.
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class UDTValue<Raw, This extends UDTValue<Raw, This>> implements Supplier<Raw>
{
	// A function to make defensive copies of the raw value
	private final Function<? super Raw, ? extends Raw> _defensiveCopier;

	// The single-argument factory of the subclass
	private final Function<? super Raw, This> _factory;

	/**
	 * @param factory a method reference to the factory of the implementing subclass.
	 * @param defensiveCopier a function to make deep, defensive copies of the raw value.
	 */
	protected UDTValue(
		final Function<? super Raw, This> factory,
		final Function<? super Raw, ? extends Raw> defensiveCopier)
	{
		_factory = factory;
		_defensiveCopier = defensiveCopier;
	}

	/** @return raw value without a defensive copy. */
	protected abstract Raw rawWithoutDefensiveCopy();

	/** @return the raw value. */
	@Override public final Raw get() { return _defensiveCopier.apply(rawWithoutDefensiveCopy()); }

	/**
	 * Wrap the raw value in another type.
	 *
	 * @param factory a constructor or factory method reference for the desired type.
	 * @param <Result> the return type.
	 * @return the output of the factory.
	 */
	public final <Result> Result getAs(final Function<? super Raw, Result> factory)
	{
		return map(raw -> raw, factory);
	}

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

	/**
	 * Build a new value of this type with the raw underlying value converted by {@code mapper}.
	 *
	 * @param mapper the mapping function to apply to the raw underlying value.
	 * @return a new instance of this type.
	 */
	public final This map(final Function<? super Raw, ? extends Raw> mapper)
	{
		final Raw mapped = mapper.apply(get());
		if (mapped.equals(get()))
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
	public final <Result> Result map(
		final UnaryOperator<Raw> mapper,
		final Function<? super Raw, ? extends Result> factory)
	{
		return factory.apply(mapper.apply(get()));
	}

	/**
	 * Test the raw value with {@code condition}.
	 *
	 * @param condition a {@link Predicate} that tests the raw value.
	 * @return {@code true} if the underlying {@link Raw} value satisfies {@code condition}; {@code false} otherwise.
	 */
	public final boolean is(final Predicate<? super Raw> condition) { return condition.test(get()); }

	/**
	 * Reverse of {@link #is(Predicate)}.
	 *
	 * @param condition a {@link Predicate} that tests the raw value.
	 * @return {@code false} if the underlying {@link Raw} value satisfies {@code condition}; {@code true} otherwise.
	 */
	public final boolean isNot(final Predicate<? super Raw> condition) { return !is(condition); }
}
