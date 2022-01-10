package org.udtopia.recycle;

import org.junit.Test;
import org.udtopia.Value;

import static java.lang.Long.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class RecyclableLongTest
{
	private static final long[] _VALUES = {MIN_VALUE + 1, -1, 0, 1, 123, MAX_VALUE - 1, MAX_VALUE};

	@RecycleBinSize(1)
	static final @Value class Count extends RecyclableLong<Count>
	{
		Count(final long rawValue) { super(Count::new, rawValue); }

		static Count parse(final String str) { return parse(Count::new, str); }
	}

	@Test public void shouldParseOwnToStringOutput()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			final String s = x.toString();
			assertThat(Count.parse(s), is(x));
		}
	}

	@Test public void shouldMarkDiscarded()
	{
		final Count x = new Count(12);
		assertThat(x.isDiscarded(), is(false));
		x.discard();
		assertThat(x.isDiscarded(), is(true));
	}

	@Test(expected = AssertionError.class) public void shouldTrapDoubleDiscard()
	{
		final Count x = new Count(12);
		x.discard();
		x.discard();
	}

	@Test public void shouldRecycleExistingInstance()
	{
		final Count x = RecyclableLong.recycle(Count.class, Count::new, 12);
		assertThat(x.getAsLong(), is(12L));
		x.discard();
		final Count y = RecyclableLong.recycle(Count.class, Count::new, 1);
		assertThat(y, is(sameInstance(x)));
		assertThat(y.getAsLong(), is(1L));
	}

	@Test public void shouldNotRecycleUndiscardedInstance()
	{
		final Count x = RecyclableLong.recycle(Count.class, Count::new, 12);
		assertThat(x.getAsLong(), is(12L));
		final Count y = RecyclableLong.recycle(Count.class, Count::new, 1);
		assertThat(y, is(not(sameInstance(x))));
		assertThat(y.getAsLong(), is(1L));
	}

	@Test(expected = AssertionError.class) public void shouldTrapAccessToDiscardedInstance()
	{
		final Count x = new Count(12);
		x.discard();
		x.getAsLong();
	}

	@Test(expected = AssertionError.class) public void shouldTrapMinValue()
	{
		RecyclableLong.recycle(Count.class, Count::new, MIN_VALUE);
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test public void shouldIgnoreMinValueInConstructor()
	{
		new Count(MIN_VALUE);
	}
}
