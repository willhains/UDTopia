package org.udtopia.recycle;

import java.util.function.Function;
import org.udtopia.ThreadSafe;
import org.udtopia.Value;

import static java.lang.ThreadLocal.*;

/**
 * Tuning parameter for how a {@link Recyclable} class will be allocated.
 */
public enum AllocationThreads
{
	/** Only one thread will ever call {@link RecycleBin#recycle}. This gives the best performance. */
	@Value SINGLE_THREADED(RingBufferRecycleBin::new),

	/**
	 * Multiple threads may call {@link RecycleBin#recycle}. This is the safest (and default) option.
	 * Each thread gets its own pool of objects, so the memory usage increases with thread count.
	 * Latency is slightly slower than {@link #SINGLE_THREADED}.
	 */
	@ThreadSafe THREAD_LOCAL(binSize ->
	{
		final ThreadLocal<RecycleBin<Recyclable>> threadLocal = withInitial(() -> new RingBufferRecycleBin<>(binSize));
		return (recycler, filler) -> threadLocal.get().recycle(recycler, filler);
	});

	private final Function<? super RingBufferSize, RecycleBin<?>> _binFactory;

	AllocationThreads(final Function<? super RingBufferSize, RecycleBin<?>> binFactory) { _binFactory = binFactory; }

	/** Create a {@link RecycleBin} with the specified level of concurrency protection. */
	final <R extends Recyclable> RecycleBin<R> recycleBin(final RingBufferSize binSize)
	{
		@SuppressWarnings("unchecked") final RecycleBin<R> bin = (RecycleBin<R>) _binFactory.apply(binSize);
		return bin;
	}
}
