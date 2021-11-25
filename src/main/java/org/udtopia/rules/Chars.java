package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

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
}
