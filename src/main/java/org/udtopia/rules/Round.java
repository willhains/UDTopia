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
 * Round numeric values to an increment (multiple).
 * The {@linkplain #toNearest increment} is {@value #DEFAULT_INCREMENT} by default.
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface Round
{
	/** The default rounding increment = {@value DEFAULT_INCREMENT}. */
	double DEFAULT_INCREMENT = 1.0;

	/**
	 * @return the rounding increment ({@value DEFAULT_INCREMENT} if omitted).
	 * 	The {@link Rule} throws {@link IllegalArgumentException} if less than or equal to zero.
	 */
	double toNearest() default DEFAULT_INCREMENT;

	/** Rule to apply {@link Round} to int, long, and double values. */
	final @Value class Rule implements IntNormalizer, LongNormalizer, DoubleNormalizer
	{
		/**
		 * Build a Round rule from an annotation.
		 *
		 * @param annotation a {@link Round} annotation.
		 * @throws IllegalArgumentException if the provided increment is less than or equal to zero.
		 */
		public Rule(final Round annotation) { this(annotation.toNearest()); }

		private final double _increment;

		Rule(final double increment)
		{
			if (increment <= 0L) { throw new IllegalArgumentException("Increment must be greater than zero"); }
			this._increment = increment;
		}

		@Override public int normalize(final int value)
		{
			return nearestInt(Math.round(Math.round(value / _increment) * _increment));
		}

		@Override public long normalize(final long value)
		{
			return Math.round(Math.round(value / _increment) * _increment);
		}

		@Override public double normalize(final double value)
		{
			return Math.round(value / _increment) * _increment;
		}

		@Override public String toString()
		{
			return format("@%s(toNearest = %s)", Round.class.getSimpleName(), _increment);
		}
	}
}
