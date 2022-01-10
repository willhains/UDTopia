package org.udtopia.recycle;

import java.util.function.Function;
import javax.annotation.Nullable;
import org.udtopia.UDTValue;
import org.udtopia.Value;
import org.udtopia.assertion.Assert;

/**
 * A recyclable value type wrapping an underlying data type.
 *
 * @param <Raw> The underlying type. Not an array.
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class RecyclableValue<Raw, This extends RecyclableValue<Raw, This>> extends UDTValue<Raw, This>
	implements Recyclable
{
	// The raw underlying value
	// Not final because we can recycle it
	// Discarded value is null
	private @Nullable Raw _raw;

	/**
	 * Use this constructor when {@code Raw} is mutable.
	 *
	 * @param factory a method reference to the factory of the implementing subclass.
	 * @param rawValue the raw, immutable value this object will represent.
	 * @param defensiveCopier a function to make deep, defensive copies of the raw value.
	 */
	protected RecyclableValue(
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
	protected RecyclableValue(final Function<? super Raw, This> factory, final Raw rawValue)
	{
		this(factory, rawValue, Function.identity());
	}

	@Override protected final Raw rawWithoutDefensiveCopy()
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
	 * @param <Raw> The underlying type.
	 * @param <This> self-reference to the subclass type itself.
	 * @return a new or recycled instance.
	 */
	protected static <Raw, This extends RecyclableValue<Raw, This>> This recycle(
		final Class<This> type,
		final Function<? super Raw, ? extends This> constructor,
		final Raw rawValue)
	{
		return RecycleBin.forClass(type).recycle(
			(RecyclableValue<Raw, This> discarded) -> discarded._raw = rawValue,
			() -> constructor.apply(rawValue));
	}
}
