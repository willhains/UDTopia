package org.udtopia.recycle;

import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.udtopia.Value;
import org.udtopia.pure.PureInt;
import org.udtopia.rules.Floor;

/**
 * Minimum size of a ring buffer.
 * Rounds the actual size up to the nearest power of two, for performance.
 */
@Floor(1) final @Value class RingBufferSize extends PureInt<RingBufferSize>
{
	// A mask to quickly compute the modulus of the buffer size
	private final int _moduloMask;

	RingBufferSize(final int size)
	{
		super(RingBufferSize::new, _roundUpToNextPowerOfTwo(size));
		_moduloMask = getAsInt() - 1;
	}

	private static int _roundUpToNextPowerOfTwo(final int size)
	{
		final int highestOneBit = Integer.highestOneBit(size);
		return size == highestOneBit ? size : highestOneBit << 1;
	}

	/**
	 * Create an array for use as a ring buffer.
	 *
	 * @param <Element> the ring buffer element type.
	 * @param arrayConstructor the array constructor (use a method reference, e.g. {@code MyObject[]::new}).
	 * @param generator a factory to generate the initial elements.
	 * @return a pre-filled array of the specified type.
	 */
	<Element> Element[] createRingBuffer(
		final IntFunction<Element[]> arrayConstructor,
		final Supplier<? extends Element> generator)
	{
		return Stream.generate(generator).limit(getAsInt()).toArray(arrayConstructor);
	}

	/**
	 * Wrap an index around to zero if it overflows the buffer size.
	 *
	 * @param index the index to increment.
	 * @return the next integer between zero (inclusive), and the buffer size (exclusive).
	 */
	int wrap(final int index)
	{
		return index & _moduloMask;
	}
}
