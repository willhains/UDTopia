package org.udtopia.recycle;

import java.util.function.Function;
import javax.annotation.Nullable;
import org.udtopia.UDTString;
import org.udtopia.Value;
import org.udtopia.assertion.Assert;

/**
 * A recyclable value type wrapping a {@link String}.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class RecyclableString<This extends RecyclableString<This>> extends UDTString<This>
	implements Recyclable
{
	// The raw underlying value
	// Not final because we can recycle it
	// Discarded value is null
	private @Nullable String _raw;

	/**
	 * @param factory a method reference to the factory of the implementing subclass.
	 * @param rawValue the raw, immutable value this object will represent.
	 */
	protected RecyclableString(final Function<? super String, This> factory, final String rawValue)
	{
		super(factory);
		_raw = applyRules(getClass(), rawValue);
	}

	@Override public final String get()
	{
		Assert.not(this::isDiscarded, "Attempted to access raw value of discarded instance!");
		return _raw;
	}

	@Override public final boolean isDiscarded()
	{
		return _raw == null;
	}

	@Override public final void discard()
	{
		Assert.not(this::isDiscarded, "Detected multiple discards on the same instance!");
		_raw = null;
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
	protected static <This extends RecyclableString<This>> This recycle(
		final Class<This> type,
		final Function<? super String, ? extends This> constructor,
		final String rawValue)
	{
		return RecycleBin.forClass(type).recycle(
			(RecyclableString<This> discarded) -> discarded._raw = applyRules(type, rawValue),
			() -> constructor.apply(rawValue));
	}
}
