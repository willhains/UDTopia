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
 * Strings containing all allowed characters.
 *
 * @see NotChars
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface Chars
{
	/** Letters (of the English alphabet). */
	String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/** Numeric digit characters. */
	String NUMERALS = "0123456789";

	/** @return a string containing all the characters allowed. */
	String value();

	/** @return when to apply this rule. */
	ApplyRuleWhen when() default ALWAYS;

	/** Rule to apply {@link Chars} to string values. */
	final @Value class Rule implements StringValidator
	{
		private final boolean[] _validCharMap;
		private final String _allowedCharacters;

		Rule(final String allowedCharacters)
		{
			_allowedCharacters = allowedCharacters;
			_validCharMap = new boolean[Character.MAX_VALUE + 1];
			allowedCharacters.chars().forEach(charIndex -> _validCharMap[charIndex] = true);
		}

		@Override public void validate(final Class<?> target, final String value)
		{
			for (final char c: value.toCharArray())
			{
				if (!_validCharMap[c])
				{
					throw new ValidationException(
						target, "\"" + value + "\" contains invalid characters (valid = " + _allowedCharacters + ")");
				}
			}
		}

		@Override public String toString()
		{
			return "@" + Chars.class.getSimpleName() + "(" + _allowedCharacters + ")";
		}
	}
}
