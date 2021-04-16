package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import javax.annotation.meta.TypeQualifierDefault;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static javax.annotation.meta.When.*;

/**
 * All code in the annotated package assumes variables, parameters, and return types are non-null by default,
 * unless otherwise annotated with {@link Nullable @Nullable}.
 */
@Documented
@TypeQualifierDefault({ANNOTATION_TYPE, CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, PARAMETER, TYPE})
@Retention(SOURCE)
public @Nonnull(when = UNKNOWN) @interface NonNullByDefault { }
