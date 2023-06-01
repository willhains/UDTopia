package org.udtopia;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Indicates that a type has thread-safe semantics.
 * <p>
 * Formally:
 * <ol>
 * <li>It is final, or all of its subtypes are guaranteed to have thread-safe semantics.</li>
 * <li>It is at least shallowly immutable, unless also annotated {@link Mutable @Mutable} (discouraged).</li>
 * <li>It might not have reliable {@link Object#equals equals}, {@link Object#hashCode hashCode},
 *     or {@link Object#toString toString} implementations.</li>
 * <li>It has no interaction with input/output whatsoever, unless also annotated {@link IO @IO} (discouraged).</li>
 * <li><b>It uses locks or other mechanisms to guarantee thread safety for some non-thread-safe object(s) .</b></li>
 * </ol>
 *
 * @see Value
 * @see Mutable
 * @see IO
 */

// Declare in Javadoc
@Documented

// Only interfaces, enums, and classes
@Target({TYPE, TYPE_USE})

// Discard at compile time
@Retention(SOURCE)

// When a supertype is declared @ThreadSafe, all must also be @ThreadSafe
@Inherited

public @interface ThreadSafe { }
