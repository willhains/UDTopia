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
 * Disallowed regex pattern for string values.
 *
 * @see Matching
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface NotMatching
{
	/** @return a regular expression pattern that values must not match. */
	String value();

	/** @return when to apply this rule. */
	ApplyRuleWhen when() default ALWAYS;

	/** Rule to apply {@link NotMatching} to string values. */
	final @Value class Rule implements StringValidator
	{
		/**
		 * Build a NotMatching rule from an annotation.
		 *
		 * @param annotation a {@link NotMatching} annotation.
		 */
		public Rule(final NotMatching annotation) { this(annotation.value()); }

		private final Pattern _pattern;

		Rule(final String regex) { _pattern = Pattern.compile(regex); }

		@Override public void validate(final Class<?> target, final String value)
		{
			if (_pattern.matcher(value).matches())
			{
				throw new ValidationException(target, "\"" + value + "\" matches pattern: " + _pattern);
			}
		}

		@Override public String toString() { return "@" + NotMatching.class.getSimpleName() + "(" + _pattern + ")"; }
	}
}
