package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Locale;
import org.udtopia.Value;

import static java.lang.String.*;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Normalize string values to {@linkplain String#toUpperCase UPPERCASE}.
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface UpperCase
{
	/** @return the locale to use for case conversion. Omit for the platform default locale. */
	String locale() default "";

	/** Rule to apply {@link UpperCase} to string values. */
	final @Value class Rule implements StringNormalizer
	{
		/**
		 * Build a UpperCase rule from an annotation.
		 *
		 * @param annotation an {@link UpperCase} annotation.
		 */
		public Rule(final UpperCase annotation) { this(annotation.locale()); }

		private final Locale _locale;

		Rule(final String locale)
		{
			_locale = locale.isEmpty() ? Locale.getDefault() : Locale.forLanguageTag(locale);
		}

		@Override public String normalize(final String value)
		{
			return value.toUpperCase(_locale);
		}

		@Override public String toString()
		{
			return format("@%s(locale = %s)", UpperCase.class.getSimpleName(), _locale.toLanguageTag());
		}
	}
}
