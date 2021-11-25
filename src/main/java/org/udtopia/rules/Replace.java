package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Replace matching substrings in string values.
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface Replace
{
	/** @return a regular expression to replace. */
	String pattern();

	/** @return the replacement string. */
	String with();
}
