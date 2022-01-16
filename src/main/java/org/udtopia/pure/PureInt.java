package org.udtopia.pure;

import java.util.function.IntFunction;
import org.udtopia.UDTInt;
import org.udtopia.Value;

/**
 * A pure, immutable value type wrapping a primitive {@code int}.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class PureInt<This extends PureInt<This>> extends UDTInt<This>
{
	// The raw underlying value
	private final int _raw;

	/**
	 * @param factory a method reference to the factory of the implementing subclass.
	 * @param rawValue the raw value this object will represent.
	 */
	protected PureInt(final IntFunction<This> factory, final int rawValue)
	{
		super(factory);
		_raw = applyRules(getClass(), rawValue);
	}

	@Override public final int getAsInt() { return _raw; }
}
