package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.annotation.meta.When;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static javax.annotation.meta.When.*;

/**
 * The annotated element must not be null.
 */
@Documented
@Retention(SOURCE)
@Target({FIELD, LOCAL_VARIABLE, PARAMETER, TYPE_PARAMETER, TYPE_USE})
public @interface Nonnull
{
	/** @return when the annotated member must not be null. */
	When when() default ALWAYS;
}
