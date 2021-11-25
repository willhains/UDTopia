package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Normalize string values to {@linkplain String#toLowerCase lowercase}.
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface LowerCase
{
	/** @return the locale to use for case conversion. Omit for the platform default locale. */
	String locale() default "";
}
