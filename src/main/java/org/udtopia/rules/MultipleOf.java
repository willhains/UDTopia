package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static org.udtopia.rules.ApplyRuleWhen.*;

/**
 * Number must be divisible by this.
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface MultipleOf
{
	/** @return the increment by which the value must be evenly divisible. */
	long value();

	/** @return when to apply this rule. */
	ApplyRuleWhen when() default ALWAYS;
}
