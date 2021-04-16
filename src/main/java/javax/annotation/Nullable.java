package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * The annotated element may be null.
 */
@Documented
@Retention(SOURCE)
@Target({FIELD, LOCAL_VARIABLE, PARAMETER, TYPE_PARAMETER, TYPE_USE})
public @interface Nullable { }
