package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Round numeric values to an increment (multiple).
 * The {@linkplain #toNearest increment} is {@value #DEFAULT_INCREMENT} by default.
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface Round
{
	/** The default rounding increment = {@value DEFAULT_INCREMENT}. */
	double DEFAULT_INCREMENT = 1.0;

	/** @return the rounding increment ({@value DEFAULT_INCREMENT} if omitted). */
	double toNearest() default DEFAULT_INCREMENT;
}
