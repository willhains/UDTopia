package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static org.udtopia.rules.ApplyRuleWhen.*;

/**
 * Upper bound (inclusive) for numeric values, or the length of strings.
 *
 * @see Min
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface Max
{
	/** @return the maximum (inclusive) value/size. */
	double value();

	/** @return when to apply this rule. */
	ApplyRuleWhen when() default ALWAYS;
}
