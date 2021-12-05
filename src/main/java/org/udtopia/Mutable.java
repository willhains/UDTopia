package org.udtopia;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Indicates that a type has cache semantics.
 * <p>
 * Formally:
 * <ol>
 * <li>It is final, or all of its subtypes are guaranteed to have cache semantics.</li>
 * <li><b>It is mutable</b>, and therefore inherently non-thread-safe.</li>
 * <li>It might not have reliable {@link Object#equals equals}, {@link Object#hashCode hashCode},
 *     or {@link Object#toString toString} implementations.</li>
 * <li>It has no interaction with input/output whatsoever, unless also annotated {@link IO @IO} (discouraged).</li>
 * <li>It has no interaction with locks or concurrency mechanisms whatsoever, unless also annotated
 *     {@link ThreadSafe @ThreadSafe} (discouraged).</li>
 * </ol>
 *
 * @see Value
 * @see IO
 * @see ThreadSafe
 */

// Declare in Javadoc
@Documented

// Only interfaces, enums, and classes
@Target({TYPE, TYPE_USE})

// Allow runtime access for assertions
@Retention(RUNTIME)

// When a supertype is declared @Mutable, all subtypes are implicitly also @Mutable
@Inherited

public @interface Mutable { }
