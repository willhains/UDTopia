package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Pattern;
import org.udtopia.Value;

import static java.lang.String.*;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Replace matching substrings in string values.
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface Replace
{
	/** @return a regular expression to replace. */
	String pattern();

	/** @return the replacement string. */
	String with();

	/** Rule to apply {@link Replace} to string values. */
	final @Value class Rule implements StringNormalizer
	{
		/**
		 * Build a {@link Replace} rule from an annotation.
		 *
		 * @param annotation a {@link Replace} annotation.
		 */
		public Rule(final Replace annotation) { this(annotation.pattern(), annotation.with()); }

		private final Pattern _pattern;
		private final String _replacement;

		Rule(final String pattern, final String replacement)
		{
			_pattern = Pattern.compile(pattern);
			_replacement = replacement;
		}

		@Override public String normalize(final String value)
		{
			return _pattern.matcher(value).replaceAll(_replacement);
		}

		@Override public String toString()
		{
			return format("@%s(pattern = %s, with = %s)", Replace.class.getSimpleName(), _pattern, _replacement);
		}
	}
}
