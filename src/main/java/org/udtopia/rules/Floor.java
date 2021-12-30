package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.udtopia.Value;

import static java.lang.String.*;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static org.udtopia.pure.PureInt.*;

/**
 * Normalize numeric values to a minimum (inclusive) value.
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface Floor
{
	/** @return the minimum allowed value. */
	double value();

	/** Rule to apply {@link Floor} to int, long, and double values. */
	final @Value class Rule implements IntNormalizer, LongNormalizer, DoubleNormalizer
	{
		private final int _intFloor;
		private final long _longFloor;
		private final double _doubleFloor;

		Rule(final double floor)
		{
			_intFloor = nearestInt(Math.round(Math.max(floor, Integer.MIN_VALUE)));
			_longFloor = Math.round(Math.max(floor, Long.MIN_VALUE));
			_doubleFloor = floor;
		}

		@Override public int normalize(final int value) { return Math.max(value, _intFloor); }

		@Override public long normalize(final long value) { return Math.max(value, _longFloor); }

		@Override public double normalize(final double value) { return Math.max(value, _doubleFloor); }

		@Override public String toString() { return format("@%s(%s)", Floor.class.getSimpleName(), _doubleFloor); }
	}
}
