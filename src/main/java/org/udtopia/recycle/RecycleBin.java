package org.udtopia.recycle;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.function.Supplier;
import org.udtopia.Mutable;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * An instance pool of {@link Recyclable} objects that have been {@linkplain Recyclable#discard discarded} and
 * can be {@linkplain Recycler#recycle recycled}.
 *
 * @param <R> Recyclable object type.
 */
@FunctionalInterface
public @Mutable interface RecycleBin<R extends Recyclable>
{
	/**
	 * @param recycler that can update the internal state of the recyclable instance.
	 * @param generator that can create a new instance if none are available in the instance pool.
	 * @return a new or recycled instance.
	 */
	R recycle(final Recycler<? super R> recycler, final Supplier<? extends R> generator);
}
