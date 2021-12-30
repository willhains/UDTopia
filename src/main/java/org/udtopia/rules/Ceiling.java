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
 * Normalize numeric values to a maximum (inclusive) value.
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface Ceiling
{
	/** @return the maximum allowed value. */
	double value();

	/** Rule to apply {@link Ceiling} to int, long, and double values. */
	final @Value class Rule implements IntNormalizer, LongNormalizer, DoubleNormalizer
	{
		private final int _intCeiling;
		private final long _longCeiling;
		private final double _doubleCeiling;

		Rule(final double ceiling)
		{
			_intCeiling = nearestInt(Math.round(Math.min(ceiling, Integer.MAX_VALUE)));
			_longCeiling = Math.round(Math.min(ceiling, Long.MAX_VALUE));
			_doubleCeiling = ceiling;
		}

		@Override public int normalize(final int value) { return Math.min(value, _intCeiling); }

		@Override public long normalize(final long value) { return Math.min(value, _longCeiling); }

		@Override public double normalize(final double value) { return Math.min(value, _doubleCeiling); }

		@Override public String toString() { return format("@%s(%s)", Ceiling.class.getSimpleName(), _doubleCeiling); }
	}
}
