package org.udtopia.pure;

import java.util.function.DoubleFunction;
import org.udtopia.UDTDouble;
import org.udtopia.Value;

/**
 * A pure, immutable value type wrapping a primitive {@code double}.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class PureDouble<This extends PureDouble<This>> extends UDTDouble<This>
{
	// The raw underlying value
	private final double _raw;

	/**
	 * @param factory a method reference to the factory of the implementing subclass.
	 * @param rawValue the raw value this object will represent.
	 */
	protected PureDouble(final DoubleFunction<This> factory, final double rawValue)
	{
		super(factory);
		_raw = rawValue;
	}

	/**
	 * Parse a string value to build a UDT value.
	 *
	 * @param factory a method reference to the factory of the implementing subclass.
	 * @param string a string representation of the value.
	 * @param <This> the UDT type to return.
	 * @return a UDT value derived from the string value.
	 */
	protected static <This extends PureDouble<This>> This parse(
		final DoubleFunction<This> factory,
		final String string)
	{
		return factory.apply(Double.parseDouble(string));
	}

	@Override public final double getAsDouble() { return _raw; }
}
