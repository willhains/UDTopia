package org.udtopia.pure;

import java.util.function.LongFunction;
import org.udtopia.UDTLong;
import org.udtopia.Value;

/**
 * A pure, immutable value type wrapping a primitive {@code long}.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class PureLong<This extends PureLong<This>> extends UDTLong<This>
{
	// The raw underlying value
	private final long _raw;

	/**
	 * @param factory a method reference to the factory of the implementing subclass.
	 * @param rawValue the raw value this object will represent.
	 */
	protected PureLong(final LongFunction<This> factory, final long rawValue)
	{
		super(factory);
		_raw = applyRules(getClass(), rawValue);
	}

	/**
	 * Parse a string value to build a UDT value.
	 *
	 * @param factory a method reference to the factory of the implementing subclass.
	 * @param string a string representation of the value.
	 * @param <This> the UDT type to return.
	 * @return a UDT value derived from the string value.
	 */
	protected static <This extends PureLong<This>> This parse(
		final LongFunction<This> factory,
		final String string)
	{
		return factory.apply(Long.parseLong(string));
	}

	@Override public final long getAsLong() { return _raw; }
}
