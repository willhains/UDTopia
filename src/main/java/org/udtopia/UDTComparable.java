package org.udtopia;

/**
 * A pure value with a {@link Comparable} underlying value.
 *
 * @param <This> self-reference to the subclass type itself.
 */
public @Value interface UDTComparable<This extends UDTComparable<This>> extends Comparable<This>
{
	/**
	 * @param that a value to compare with.
	 * @return the largest of {@code this} and {@code that}.
	 */
	default This max(final This that)
	{
		@SuppressWarnings("unchecked") final This self = (This) this;
		return compareTo(that) >= 0 ? self : that;
	}

	/**
	 * @param that a value to compare with.
	 * @return the smallest of {@code this} and {@code that}.
	 */
	default This min(final This that)
	{
		@SuppressWarnings("unchecked") final This self = (This) this;
		return compareTo(that) <= 0 ? self : that;
	}
}
