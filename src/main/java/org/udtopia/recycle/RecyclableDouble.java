package org.udtopia.recycle;

import java.util.function.DoubleFunction;
import org.udtopia.UDTDouble;
import org.udtopia.Value;
import org.udtopia.assertion.Assert;

/**
 * A recyclable value type wrapping a primitive {@code double}.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public abstract @Value class RecyclableDouble<This extends RecyclableDouble<This>> extends UDTDouble<This>
	implements Recyclable
{
	// The raw underlying value
	// Not final because we can recycle it
	// Discarded value is NaN
	private double _raw;

	/**
	 * @param factory a method reference to the factory of the implementing subclass.
	 * @param rawValue the raw, immutable value this object will represent.
	 */
	protected RecyclableDouble(final DoubleFunction<This> factory, final double rawValue)
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
	protected static <This extends RecyclableDouble<This>> This parse(
		final DoubleFunction<This> factory,
		final String string)
	{
		return factory.apply(Double.parseDouble(string));
	}

	@Override public final double getAsDouble()
	{
		Assert.not(this::isDiscarded, "Attempted to access raw value of discarded instance!");
		return _raw;
	}

	@Override public final boolean isDiscarded()
	{
		return Double.isNaN(_raw);
	}

	@Override public final void discard()
	{
		Assert.not(this::isDiscarded, "Detected multiple discards on the same instance!");
		_raw = Double.NaN;
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
	protected static <This extends RecyclableDouble<This>> This recycle(
		final Class<This> type,
		final DoubleFunction<? extends This> constructor,
		final double rawValue)
	{
		Assert.not(() -> Double.isNaN(rawValue), "NaN is not allowed for RecyclableDouble subclasses.");
		return RecycleBin.forClass(type).recycle(
			(RecyclableDouble<This> discarded) -> discarded._raw = applyRules(type, rawValue),
			() -> constructor.apply(rawValue));
	}
}
