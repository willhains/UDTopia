package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Pattern;
import org.udtopia.Value;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static org.udtopia.rules.ApplyRuleWhen.*;

/**
 * Allowed regex patterns for string values.
 *
 * @see NotMatching
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface Matching
{
	/** @return a regular expression pattern that values must match. */
	String value();

	/** @return when to apply this rule. */
	ApplyRuleWhen when() default ALWAYS;

	/** Rule to apply {@link Matching} to string values. */
	final @Value class Rule implements StringValidator
	{
		private final Pattern _pattern;

		Rule(final String regex) { _pattern = Pattern.compile(regex); }

		@Override public void validate(final Class<?> target, final String value)
		{
			if (!_pattern.matcher(value).matches())
			{
				throw new ValidationException(target, "\"" + value + "\" does not match pattern: " + _pattern);
			}
		}

		@Override public String toString() { return "@" + Matching.class.getSimpleName() + "(" + _pattern + ")"; }
	}
}
