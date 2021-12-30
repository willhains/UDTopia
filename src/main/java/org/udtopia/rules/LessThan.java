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
 * Numeric upper bound (exclusive) for numeric values, or the length of strings.
 *
 * @see GreaterThan
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface LessThan
{
	/** @return the maximum (exclusive) value/size. */
	double value();

	/** @return when to apply this rule. */
	ApplyRuleWhen when() default ALWAYS;

	/** Rule to apply {@link LessThan} to int, long, and double values. */
	final @Value class Rule implements IntValidator, LongValidator, DoubleValidator
	{
		private final double _bound;

		Rule(final double bound) { _bound = bound; }

		@Override public void validate(final Class<?> target, final int value) { _check(target, value); }

		@Override public void validate(final Class<?> target, final long value) { _check(target, value); }

		@Override public void validate(final Class<?> target, final double value) { _check(target, value); }

		private void _check(final Class<?> target, final double value)
		{
			if (value >= _bound)
			{
				throw new ValidationException(target, value + " >= " + _bound);
			}
		}

		@Override public String toString() { return "@" + LessThan.class.getSimpleName() + "(" + _bound + ")"; }
	}
}
