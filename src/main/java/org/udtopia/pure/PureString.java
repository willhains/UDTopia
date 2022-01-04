package org.udtopia.pure;

import java.util.function.Function;
import org.udtopia.UDTString;
import org.udtopia.Value;
import org.udtopia.assertion.Assert;

/**
 * A pure, immutable value type wrapping a {@link String}.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class PureString<This extends PureString<This>> extends UDTString<This>
{
	// The raw underlying value
	private final String _raw;

	/**
	 * @param factory a method reference to the factory of the implementing subclass.
	 * @param rawValue the raw value this object will represent.
	 */
	protected PureString(final Function<? super String, This> factory, final String rawValue)
	{
		super(factory);
		Assert.notNull(() -> rawValue, "Raw value must not be null");
		_raw = rawValue;
	}

	@Override public final String get() { return _raw; }
}
