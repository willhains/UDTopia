package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.udtopia.Value;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static org.udtopia.rules.ApplyRuleWhen.*;

/**
 * Upper bound (inclusive) for numeric values, or the length of strings.
 *
 * @see Min
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface Max
{
	/** @return the maximum (inclusive) value/size. */
	double value();

	/** @return when to apply this rule. */
	ApplyRuleWhen when() default ALWAYS;

	/** Rule to apply {@link Max} to int, long, double, and string values. */
	final @Value class Rule implements IntValidator, LongValidator, DoubleValidator, StringValidator
	{

		/**
		 * Build a Max rule from an annotation.
		 *
		 * @param annotation a {@link Max} annotation.
		 */
		public Rule(final Max annotation) { this(annotation.value()); }

		private final double _max;

		Rule(final double max) { _max = max; }

		@Override public void validate(final Class<?> target, final int value) { _check(target, value); }

		@Override public void validate(final Class<?> target, final long value) { _check(target, value); }

		@Override public void validate(final Class<?> target, final double value) { _check(target, value); }

		@Override public void validate(final Class<?> target, final String value)
		{
			final int length = value.length();
			if (length > _max)
			{
				throw new ValidationException(target, "length > " + _max, value);
			}
		}

		private void _check(final Class<?> target, final double value)
		{
			if (value > _max) { throw new ValidationException(target, value + " > " + _max); }
		}

		@Override public String toString() { return "@" + Max.class.getSimpleName() + "(" + _max + ")"; }
	}
}
