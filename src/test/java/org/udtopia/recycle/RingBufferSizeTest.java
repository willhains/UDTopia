package org.udtopia.recycle;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class RingBufferSizeTest
{
	@Test public void shouldNormalizeZeroToFloor()
	{
		assertThat(new RingBufferSize(0).getAsInt(), is(1));
	}

	@Test public void shouldNormalizeNegativeToFloor()
	{
		assertThat(new RingBufferSize(-1).getAsInt(), is(1));
	}

	@Test public void shouldNotRoundUpWhenAlreadyPowerOfTwo()
	{
		assertThat(new RingBufferSize(16).getAsInt(), is(16));
	}

	@Test public void shouldRoundUpToNextPowerOfTwoWhenOneLower()
	{
		assertThat(new RingBufferSize(15).getAsInt(), is(16));
	}

	@Test public void shouldRoundUpToNextPowerOfTwoWhenOneHigher()
	{
		assertThat(new RingBufferSize(17).getAsInt(), is(32));
	}

	@Test public void shouldFillArray()
	{
		final String[] array = new RingBufferSize(4).createRingBuffer(String[]::new, () -> "x");
		assertThat(Arrays.stream(array).allMatch("x"::equals), is(true));
	}

	@Test public void shouldCallGeneratorForEachElement()
	{
		final AtomicInteger count = new AtomicInteger();
		new RingBufferSize(8).createRingBuffer(String[]::new, () ->
		{
			count.incrementAndGet();
			return "x";
		});
		assertThat(count.get(), is(8));
	}
}
