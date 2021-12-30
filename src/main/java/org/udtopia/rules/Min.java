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
 * Lower bound (inclusive) for numeric values, or the length of strings.
 *
 * @see Max
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface Min
{
	/** @return the minimum (inclusive) value/size. */
	double value();

	/** @return when to apply this rule. */
	ApplyRuleWhen when() default ALWAYS;

	/** Rule to apply {@link Min} to int, long, double, and string values. */
	final @Value class Rule implements IntValidator, LongValidator, DoubleValidator, StringValidator
	{
		private final double _min;

		Rule(final double min) { _min = min; }

		@Override public void validate(final Class<?> target, final int value) { _check(target, value); }

		@Override public void validate(final Class<?> target, final long value) { _check(target, value); }

		@Override public void validate(final Class<?> target, final double value) { _check(target, value); }

		@Override public void validate(final Class<?> target, final String value)
		{
			if (value.length() < _min)
			{
				throw new ValidationException(target, "length < " + _min + ": \"" + value + "\"");
			}
		}

		private void _check(final Class<?> target, final double value)
		{
			if (value < _min) { throw new ValidationException(target, value + " < " + _min); }
		}

		@Override public String toString() { return "@" + Min.class.getSimpleName() + "(" + _min + ")"; }
	}
}
