package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
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
	/**
	 * @return a regular expression pattern that values must match.
	 * 	The {@link Rule} throws {@link PatternSyntaxException} if invalid.
	 */
	String value();

	/** @return when to apply this rule. */
	ApplyRuleWhen when() default ALWAYS;

	/** Rule to apply {@link Matching} to string values. */
	final @Value class Rule implements StringValidator
	{
		/**
		 * Build a Matching rule from an annotation.
		 *
		 * @param annotation a {@link Matching} annotation.
		 * @throws PatternSyntaxException if the provided regular expression pattern is invalid.
		 */
		public Rule(final Matching annotation) { this(annotation.value()); }

		private final Pattern _pattern;

		Rule(final String regex) { _pattern = Pattern.compile(regex); }

		@Override public void validate(final Class<?> target, final String value)
		{
			if (!_pattern.matcher(value).matches())
			{
				throw new ValidationException(target, "does not match valid pattern " + _pattern, value);
			}
		}

		@Override public String toString() { return "@" + Matching.class.getSimpleName() + "(" + _pattern + ")"; }
	}
}
