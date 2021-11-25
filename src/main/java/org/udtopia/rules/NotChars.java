package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

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
}
