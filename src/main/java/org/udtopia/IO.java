package org.udtopia;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Indicates that a type has controller semantics.
 * <p>
 * Formally:
 * <ol>
 * <li>It is final, or all of its subtypes are guaranteed to have controller semantics.</li>
 * <li>It is at least shallowly immutable, unless also annotated {@link Mutable @Mutable} (discouraged).</li>
 * <li>It might not have reliable {@link Object#equals equals}, {@link Object#hashCode hashCode},
 *     or {@link Object#toString toString} implementations.</li>
 * <li><b>It interacts with input and/or output.</b></li>
 * <li>It has no interaction with locks or concurrency mechanisms whatsoever, unless also annotated
 *     {@link ThreadSafe @ThreadSafe} (discouraged).</li>
 * </ol>
 *
 * @see Value
 * @see Mutable
 * @see ThreadSafe
 */

// Declare in Javadoc
@Documented

// Only interfaces, enums, and classes
@Target({TYPE, TYPE_USE})

// Discard at compile time
@Retention(SOURCE)

// When a supertype is declared @IO, all subtypes are implicitly also @IO
@Inherited

public @interface IO { }
