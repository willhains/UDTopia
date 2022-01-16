package org.udtopia.recycle;

import java.util.function.IntFunction;
import org.udtopia.UDTInt;
import org.udtopia.Value;
import org.udtopia.assertion.Assert;

/**
 * A recyclable value type wrapping a primitive {@code int}.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class RecyclableInt<This extends RecyclableInt<This>> extends UDTInt<This>
	implements Recyclable
{
	// The raw underlying value
	// Not final because we can recycle it
	// Discarded value is MIN_VALUE
	private int _raw;

	/**
	 * @param factory a method reference to the factory of the implementing subclass.
	 * @param rawValue the raw, immutable value this object will represent.
	 */
	protected RecyclableInt(final IntFunction<This> factory, final int rawValue)
	{
		super(factory);
		_raw = applyRules(getClass(), rawValue);
	}

	@Override public final int getAsInt()
	{
		Assert.not(this::isDiscarded, "Attempted to access raw value of discarded instance!");
		return _raw;
	}

	@Override public final boolean isDiscarded()
	{
		return _raw == Integer.MIN_VALUE;
	}

	@Override public final void discard()
	{
		Assert.not(this::isDiscarded, "Detected multiple discards on the same instance!");
		_raw = Integer.MIN_VALUE;
	}

	/**
	 * Attempt to recycle an instance, with the specified new value.
	 * If no instances are available to recycle, create a new instance.
	 *
	 * @param type the recycled class.
	 * @param constructor a method reference to the constructor of the class.
	 * @param rawValue the new value.
	 * @param <This> self-reference to the subclass type itself.
	 * @return a new or recycled instance.
	 */
	protected static <This extends RecyclableInt<This>> This recycle(
		final Class<This> type,
		final IntFunction<? extends This> constructor,
		final int rawValue)
	{
		Assert.not(() -> rawValue == Integer.MIN_VALUE, "MIN_VALUE is not allowed for RecyclableInt subclasses.");
		return RecycleBin.forClass(type).recycle(
			(RecyclableInt<This> discarded) -> discarded._raw = applyRules(type, rawValue),
			() -> constructor.apply(rawValue));
	}
}
