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

	/**
	 * @param that a value to compare with.
	 * @return {@code true} if {@code this} is greater than (but not equal to) {@code that}.
	 */
	default boolean isGreaterThan(final This that) { return compareTo(that) > 0; }

	/**
	 * @param that a value to compare with.
	 * @return {@code true} if {@code this} is greater than or equal to {@code that}.
	 */
	default boolean isGreaterThanOrEqualTo(final This that) { return compareTo(that) >= 0; }

	/**
	 * @param that a value to compare with.
	 * @return {@code true} if {@code this} is less than (but not equal to) {@code that}.
	 */
	default boolean isLessThan(final This that) { return compareTo(that) < 0; }

	/**
	 * @param that a value to compare with.
	 * @return {@code true} if {@code this} is less than or equal to {@code that}.
	 */
	default boolean isLessThanOrEqualTo(final This that) { return compareTo(that) <= 0; }
}
