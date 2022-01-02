package org.udtopia.recycle;

import java.util.function.Supplier;
import org.udtopia.Mutable;
import org.udtopia.Value;

import static java.lang.String.*;

/**
 * High-performance implementation of {@link RecycleBin} that prioritizes latency/throughput over garbage reduction.
 *
 * @param <R> the {@link Recyclable} class to be pooled.
 */
public final @Mutable class RingBufferRecycleBin<R extends Recyclable> implements RecycleBin<R>
{
	// Ring buffer array of instances
	private final R[] _bin;

	// The size of the ring buffer
	private final RingBufferSize _binSize;

	// Total count of recycle attempts
	private int _count;

	@SuppressWarnings({"unchecked", "SuspiciousArrayCast"}) RingBufferRecycleBin(final RingBufferSize size)
	{
		// Fill array with a null object that is unavailable for recycling
		_bin = (R[]) size.createRingBuffer(Recyclable[]::new, () -> DUMMY);
		_binSize = size;
	}

	static final Recyclable DUMMY = new @Value Recyclable()
	{
		@SuppressWarnings("MethodReturnAlwaysConstant")
		@Override public boolean isDiscarded() { return false; }

		@Override public void discard() { }
	};

	@Override public R recycle(final Recycler<? super R> recycler, final Supplier<? extends R> generator)
	{
		final R[] bin = _bin;

		// Increment head index
		// Wrap around to 0 when it reaches the end, so it acts as a ring buffer
		final int head = _getAndAdvanceHeadIndex();

		// Recycle the oldest instance in the bin
		R instance = bin[head];
		if (instance.isDiscarded()) { recycler.recycle(instance); }
		else
		{
			// No instances available for recycling; replace head instance with a new one
			// The replaced instance will go to GC eventually
			instance = generator.get();
			bin[head] = instance;
		}

		return instance;
	}

	private int _getAndAdvanceHeadIndex()
	{
		final int head = _binSize.wrap(_count);
		if (_count == Integer.MAX_VALUE)
		{
			_count = 0;
		}
		else { _count++; }
		return head;
	}

	@Override public String toString()
	{
		return format("RecycleBin[%,d]", _binSize.getAsInt());
	}
}
