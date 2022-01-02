package org.udtopia.recycle;

import java.util.Optional;
import java.util.function.Supplier;
import org.udtopia.Mutable;
import org.udtopia.ThreadSafe;

import static org.udtopia.recycle.RecycleBinSize.*;

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

	/**
	 * @param recyclable the {@link Recyclable} class.
	 * @param <R> the {@link Recyclable} class.
	 * @return the recycle bin for the specified {@link Recyclable} class.
	 */
	@SuppressWarnings("unchecked")
	static <R extends Recyclable> RecycleBin<R> forClass(final Class<R> recyclable)
	{
		return (RecycleBin<R>) FOR_CLASS.get(recyclable);
	}

	/**
	 * Lazy store of recycle bins for every {@link Recyclable} class.
	 *
	 * @see #forClass(Class) method to avoid having to cast the return value.
	 */
	ClassValue<RecycleBin<?>> FOR_CLASS = new @ThreadSafe @Mutable ClassValue<RecycleBin<?>>()
	{
		@Override protected RecycleBin<?> computeValue(final Class<?> type)
		{
			// Read annotation, or use defaults
			final Optional<RecycleBinSize> size = Optional.ofNullable(type.getAnnotation(RecycleBinSize.class));
			final RingBufferSize binSize = new RingBufferSize(size.map(RecycleBinSize::value).orElse(DEFAULT_SIZE));

			// Create recycle bin
			return new RingBufferRecycleBin<>(binSize);
		}
	};
}
