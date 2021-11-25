package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Normalize numeric values to a minimum (inclusive) value.
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface Floor
{
	/** @return the minimum allowed value. */
	double value();
}
