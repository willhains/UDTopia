package org.udtopia;

/**
 * A pure value with a {@link Comparable} underlying value.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public @Value interface UDTComparable<This extends UDTComparable<This>> extends Comparable<This>
{
}
