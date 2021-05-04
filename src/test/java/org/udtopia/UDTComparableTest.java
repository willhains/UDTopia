package org.udtopia;

import java.util.stream.Stream;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class UDTComparableTest
{
	static final @Value class Counter implements UDTComparable<Counter>
	{
		final int raw;

		Counter(final int rawValue) { this.raw = rawValue; }

		@Override public int compareTo(final Counter that)
		{
			return Integer.compare(this.raw, that.raw);
		}

		@Override public boolean equals(final Object obj) { return obj instanceof Counter && eq((Counter) obj); }

		public boolean eq(final Counter that) { return this == that || this.raw == that.raw; }

		@Override public int hashCode() { return Integer.hashCode(raw); }
	}

	private final Counter _c1 = new Counter(3);
	private final Counter _c2 = new Counter(5);
	private final Counter _c3 = new Counter(3);

	@Test public void shouldReturnLarger()
	{
		assertThat(_c1.max(_c2), is(sameInstance(_c2)));
		assertThat(_c2.max(_c1), is(sameInstance(_c2)));
		assertThat(_c1.max(_c1), is(sameInstance(_c1)));
	}

	@Test public void shouldReturnSmaller()
	{
		assertThat(_c1.min(_c2), is(sameInstance(_c1)));
		assertThat(_c2.min(_c1), is(sameInstance(_c1)));
		assertThat(_c2.min(_c2), is(sameInstance(_c2)));
	}

	@Test public void shouldReturnSame()
	{
		assertThat(_c1.min(_c3), is(sameInstance(_c1)));
		assertThat(_c3.min(_c1), is(sameInstance(_c3)));
		assertThat(_c1.max(_c3), is(sameInstance(_c1)));
		assertThat(_c3.max(_c1), is(sameInstance(_c3)));
	}

	@Test public void shouldReturnMax()
	{
		assertThat(Stream.of(_c1, _c2, _c3).reduce(Counter::max).get(), is(sameInstance(_c2)));
		assertThat(Stream.of(_c1, _c2, _c3).max(Counter::compareTo).get(), is(sameInstance(_c2)));
	}

	@Test public void shouldReturnMin()
	{
		assertThat(Stream.of(_c1, _c2, _c3).reduce(Counter::min).get(), is(sameInstance(_c1)));
		assertThat(Stream.of(_c1, _c2, _c3).min(Counter::compareTo).get(), is(sameInstance(_c1)));
	}
}
