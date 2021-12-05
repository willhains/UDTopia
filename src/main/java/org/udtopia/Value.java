package org.udtopia;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Indicates that a type has pure value semantics.
 * <p>
 * Formally:
 * <ol>
 * <li>It is final, or all of its subtypes are guaranteed to have pure value semantics.</li>
 * <li><b>It is strictly and deeply immutable</b>, and therefore inherently thread-safe.</li>
 * <li>It has correct {@link Object#equals equals}, {@link Object#hashCode hashCode},
 *     and {@link Object#toString toString} implementations.</li>
 * <li>It has no interaction with input/output whatsoever.</li>
 * <li>It has no interaction with locks or concurrency mechanisms whatsoever.</li>
 * <li>It is not annotated {@link Mutable @Mutable}, {@link IO @IO}, or {@link ThreadSafe @ThreadSafe}.</li>
 * </ol>
 *
 * @see Mutable
 * @see IO
 * @see ThreadSafe
 */

// Declare in Javadoc
@Documented

// Allow generic @Value types to require @Value type parameters
@Target({TYPE, TYPE_PARAMETER, TYPE_USE})

// Allow runtime access for assertions
@Retention(RUNTIME)

// When a supertype is declared @Value, all subtypes must also be @Value
@Inherited

public @interface Value { }
