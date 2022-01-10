package org.udtopia.recycle;

import org.junit.Test;
import org.udtopia.Value;

import static java.lang.Double.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class RecyclableDoubleTest
{
	private static final double[] _VALUES = {
		NEGATIVE_INFINITY, -MAX_VALUE, -1.5, -MIN_VALUE, -0.0,
		0.0, MIN_VALUE, 0.1, 123.0, MAX_VALUE, POSITIVE_INFINITY};

	@RecycleBinSize(1)
	static final @Value class Height extends RecyclableDouble<Height>
	{
		Height(final double rawValue) { super(Height::new, rawValue); }

		static Height parse(final String str) { return parse(Height::new, str); }
	}

	@Test public void shouldParseOwnToStringOutput()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			final String s = x.toString();
			assertThat(Height.parse(s), is(x));
		}
	}

	@Test public void shouldMarkDiscarded()
	{
		final Height x = new Height(12.0);
		assertThat(x.isDiscarded(), is(false));
		x.discard();
		assertThat(x.isDiscarded(), is(true));
	}

	@Test(expected = AssertionError.class) public void shouldTrapDoubleDiscard()
	{
		final Height x = new Height(12.0);
		x.discard();
		x.discard();
	}

	@Test public void shouldRecycleExistingInstance()
	{
		final Height x = RecyclableDouble.recycle(Height.class, Height::new, 12.0);
		assertThat(x.getAsDouble(), is(12.0));
		x.discard();
		final Height y = RecyclableDouble.recycle(Height.class, Height::new, 1.0);
		assertThat(y, is(sameInstance(x)));
		assertThat(y.getAsDouble(), is(1.0));
	}

	@Test public void shouldNotRecycleUndiscardedInstance()
	{
		final Height x = RecyclableDouble.recycle(Height.class, Height::new, 12.0);
		assertThat(x.getAsDouble(), is(12.0));
		final Height y = RecyclableDouble.recycle(Height.class, Height::new, 1.0);
		assertThat(y, is(not(sameInstance(x))));
		assertThat(y.getAsDouble(), is(1.0));
	}

	@Test(expected = AssertionError.class) public void shouldTrapAccessToDiscardedInstance()
	{
		final Height x = new Height(12.0);
		x.discard();
		x.getAsDouble();
	}

	@Test(expected = AssertionError.class) public void shouldTrapNaN()
	{
		RecyclableDouble.recycle(Height.class, Height::new, NaN);
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test public void shouldIgnoreNaNInConstructor()
	{
		new Height(NaN);
	}
}
