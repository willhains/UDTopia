package org.udtopia.recycle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.udtopia.recycle.AllocationThreads.*;

public class AllocationThreadsTest
{
	private static final RingBufferSize _BIN_SIZE = new RingBufferSize(1);

	static class A implements Recyclable
	{
		@Override public boolean isDiscarded() { return true; }

		@Override public void discard() { }
	}

	@Test public void singleThreadShouldUseOneInstancePool() throws Exception
	{
		final RecycleBin<A> bin = SINGLE_THREADED.recycleBin(new RingBufferSize(1));
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		try
		{
			final A a1 = bin.recycle(System.out::println, A::new);
			assertThat(bin.recycle(System.out::println, A::new), is(sameInstance(a1)));

			final A a2 = thread.submit(() -> bin.recycle(System.err::println, A::new)).get();
			assertThat(a2, is(sameInstance(a1)));
		}
		finally { thread.shutdown(); }
	}

	@Test public void multipleThreadsShouldUseSeparateInstancePools() throws Exception
	{
		final RecycleBin<A> bin = THREAD_LOCAL.recycleBin(_BIN_SIZE);
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		try
		{
			final A a1 = bin.recycle(System.out::println, A::new);
			assertThat(bin.recycle(System.out::println, A::new), is(sameInstance(a1)));

			final A a2 = thread.submit(() -> bin.recycle(System.err::println, A::new)).get();
			assertThat(a2, is(not(sameInstance(a1))));
			assertThat(thread.submit(() -> bin.recycle(System.out::println, A::new)).get(), is(sameInstance(a2)));
		}
		finally { thread.shutdown(); }
	}
}
