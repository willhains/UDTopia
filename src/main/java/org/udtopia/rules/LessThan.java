package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static org.udtopia.rules.ApplyRuleWhen.*;

/**
 * Numeric upper bound (exclusive) for numeric values, or the length of strings.
 *
 * @see GreaterThan
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface LessThan
{
	/** @return the maximum (exclusive) value/size. */
	double value();

	/** @return when to apply this rule. */
	ApplyRuleWhen when() default ALWAYS;
}