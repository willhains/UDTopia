package org.udtopia;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

/**
 * A wrapped numeric value.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public @Value interface UDTNumber<This extends UDTNumber<This>>
	extends UDTComparable<This>, IntSupplier, LongSupplier, DoubleSupplier
{
}
