package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.udtopia.Value;

import static java.lang.String.*;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Trim from the beginning and/or end of string values.
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface Trim
{
	/** Rule to apply {@link Trim} to string values. */
	final @Value class Rule implements StringNormalizer
	{
		/**
		 * Build a Trim rule from an annotation.
		 *
		 * @param annotation a {@link Trim} annotation.
		 */
		public Rule(final Trim annotation) { this(); }

		Rule() { }

		@Override public String normalize(final String value)
		{
			return value.trim();
		}

		@Override public String toString() { return format("@%s", Trim.class.getSimpleName()); }
	}
}
