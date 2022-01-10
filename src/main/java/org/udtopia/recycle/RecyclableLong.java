package org.udtopia.recycle;

import java.util.function.LongFunction;
import org.udtopia.UDTLong;
import org.udtopia.Value;
import org.udtopia.assertion.Assert;

/**
 * A recyclable value type wrapping a primitive {@code int}.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class RecyclableLong<This extends RecyclableLong<This>> extends UDTLong<This>
	implements Recyclable
{
	// The raw underlying value
	// Not final because we can recycle it
	// Discarded value is MIN_VALUE
	private long _raw;

	/**
	 * @param factory a method reference to the factory of the implementing subclass.
	 * @param rawValue the raw, immutable value this object will represent.
	 */
	protected RecyclableLong(final LongFunction<This> factory, final long rawValue)
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
	protected static <This extends RecyclableLong<This>> This parse(
		final LongFunction<This> factory,
		final String string)
	{
		return factory.apply(Long.parseLong(string));
	}

	@Override public final long getAsLong()
	{
		Assert.not(this::isDiscarded, "Attempted to access raw value of discarded instance!");
		return _raw;
	}

	@Override public final boolean isDiscarded()
	{
		return _raw == Long.MIN_VALUE;
	}

	@Override public final void discard()
	{
		Assert.not(this::isDiscarded, "Detected multiple discards on the same instance!");
		_raw = Long.MIN_VALUE;
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
	protected static <This extends RecyclableLong<This>> This recycle(
		final Class<This> type,
		final LongFunction<? extends This> constructor,
		final long rawValue)
	{
		Assert.not(() -> rawValue == Long.MIN_VALUE, "MIN_VALUE is not allowed for RecyclableLong classes.");
		return RecycleBin.forClass(type).recycle(
			(RecyclableLong<This> discarded) -> discarded._raw = applyRules(type, rawValue),
			() -> constructor.apply(rawValue));
	}
}
