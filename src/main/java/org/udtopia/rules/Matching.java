package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

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
}
