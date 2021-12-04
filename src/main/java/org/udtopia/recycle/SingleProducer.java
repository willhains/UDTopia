package org.udtopia.recycle;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Turn off thread safety for {@link RecycleBin#recycle}.
 * When only one thread will recycle instances, this option may be faster.
 * Note that {@link Recyclable#discard()} is always safe to call from any thread.
 *
 * @see AllocationThreads#SINGLE_THREADED
 * @see RecycleBin#recycle
 * @see Recyclable#discard
 */
@Target(TYPE) @Retention(RUNTIME)
public @interface SingleProducer { }
