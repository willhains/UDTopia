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
 * Number must be divisible by this.
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface MultipleOf
{
	/**
	 * @return the increment by which the value must be evenly divisible.
	 * 	The {@link Rule} throws {@link IllegalArgumentException} if less than or equal to zero.
	 */
	long value();

	/** @return when to apply this rule. */
	ApplyRuleWhen when() default ALWAYS;

	/** Rule to apply {@link MultipleOf} to int, long, and double values. */
	final @Value class Rule implements IntValidator, LongValidator
	{
		/**
		 * Build a MultipleOf rule from an annotation.
		 *
		 * @param annotation a {@link MultipleOf} annotation.
		 * @throws IllegalArgumentException if the provided increment is less than or equal to zero.
		 */
		public Rule(final MultipleOf annotation) { this(annotation.value()); }

		private final long _increment;

		Rule(final long increment)
		{
			if (increment <= 0L) { throw new IllegalArgumentException("Increment must be greater than zero"); }
			_increment = increment;
		}

		@Override public void validate(final Class<?> target, final int value) { _check(target, value); }

		@Override public void validate(final Class<?> target, final long value) { _check(target, value); }

		private void _check(final Class<?> target, final long value)
		{
			if (value % _increment != 0.0)
			{
				throw new ValidationException(target, value + " is not a multiple of " + _increment);
			}
		}

		@Override public String toString() { return "@" + MultipleOf.class.getSimpleName() + "(" + _increment + ")"; }
	}
}
