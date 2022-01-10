package org.udtopia.example.custom;

import java.lang.annotation.Retention;
import org.udtopia.Value;
import org.udtopia.rules.ApplyRuleWhen;
import org.udtopia.rules.DoubleValidator;
import org.udtopia.rules.LongValidator;
import org.udtopia.rules.ValidationException;

import static java.lang.annotation.RetentionPolicy.*;
import static org.udtopia.rules.ApplyRuleWhen.*;

// Rule to validate that the value is a power of a specified base
@Retention(RUNTIME)
public @interface PowerOf
{
	double value();

	ApplyRuleWhen when() default ALWAYS;

	final @Value class Rule implements DoubleValidator, LongValidator
	{
		private final double _base;

		public Rule(final PowerOf ann) { this(ann.value()); }

		Rule(final double base) { _base = base; }

		void check(final Class<?> target, final double value)
		{
			if (Math.abs(Math.log(value) / Math.log(_base)) % 1.0 != 0.0)
			{
				throw new ValidationException(target, value + " is not a power of " + _base);
			}
		}

		@Override public void validate(final Class<?> c, final double val) { check(c, val); }

		@Override public void validate(final Class<?> c, final long val) { check(c, val); }
	}
}
