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

	@Override public final long getAsLong() { return _raw; }
}
