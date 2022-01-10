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
 * Characters that must not appear anywhere in the raw string value.
 *
 * @see Chars
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface NotChars
{
	/** @return a string containing all the characters not allowed. */
	String value();

	/** @return when to apply this rule. */
	ApplyRuleWhen when() default ALWAYS;

	/** Rule to apply {@link NotChars} to string values. */
	final @Value class Rule implements StringValidator
	{
		/**
		 * Build a NotChars rule from an annotation.
		 *
		 * @param annotation a {@link NotChars} annotation.
		 */
		public Rule(final NotChars annotation) { this(annotation.value()); }

		private final boolean[] _invalidCharMap;
		private final String _disallowedCharacters;

		Rule(final String disallowedCharacters)
		{
			_disallowedCharacters = disallowedCharacters;
			_invalidCharMap = new boolean[Character.MAX_VALUE + 1];
			disallowedCharacters.chars().forEach(charIndex -> _invalidCharMap[charIndex] = true);
		}

		@Override public void validate(final Class<?> target, final String value)
		{
			for (final char c: value.toCharArray())
			{
				if (_invalidCharMap[c])
				{
					throw new ValidationException(
						target,
						"\"" + value + "\" contains invalid characters (invalid = " + _disallowedCharacters + ")");
				}
			}
		}

		@Override public String toString()
		{
			return "@" + NotChars.class.getSimpleName() + "(" + _disallowedCharacters + ")";
		}
	}
}
