package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

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
}
