package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Trim from the beginning and/or end of string values.
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface Trim
{
}
