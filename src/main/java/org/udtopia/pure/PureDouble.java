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
		_raw = applyRules(getClass(), rawValue);
	}

	@Override public final double getAsDouble() { return _raw; }
}
