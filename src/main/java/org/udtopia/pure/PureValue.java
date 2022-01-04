package org.udtopia.pure;

import java.util.function.Function;
import org.udtopia.UDTValue;
import org.udtopia.Value;
import org.udtopia.assertion.Assert;

/**
 * A pure, immutable value type wrapping an underlying data type.
 *
 * @param <Raw> the underlying type. Not an array.
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class PureValue<Raw, This extends PureValue<Raw, This>> extends UDTValue<Raw, This>
{
	// The raw underlying value
	private final Raw _raw;

	/**
	 * Use this constructor when {@code Raw} is mutable.
	 *
	 * @param factory a method reference to the factory of the implementing subclass.
	 * @param rawValue the raw value this object will represent.
	 * @param defensiveCopier a function to make deep, defensive copies of the raw value.
	 */
	protected PureValue(
		final Function<? super Raw, This> factory,
		final Raw rawValue,
		final Function<? super Raw, ? extends Raw> defensiveCopier)
	{
		super(factory, defensiveCopier);
		Assert.notNull(() -> rawValue, "Raw value must not be null");
		Assert.not(() -> rawValue.getClass().isArray(), "Raw value must not be an array");
		_raw = defensiveCopier.apply(rawValue);
	}

	/**
	 * Use this constructor when {@code Raw} is immutable.
	 *
	 * @param factory a method reference to the factory of the implementing subclass.
	 * @param rawValue the raw value this object will represent.
	 */
	protected PureValue(final Function<? super Raw, This> factory, final Raw rawValue)
	{
		this(factory, rawValue, Function.identity());
	}

	// Internal accessor of _raw without defensive copy
	@Override protected final Raw rawWithoutDefensiveCopy() { return _raw; }
}
