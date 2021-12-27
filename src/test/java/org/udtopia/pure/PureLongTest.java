package org.udtopia.pure;

import java.text.DecimalFormat;
import org.junit.Test;
import org.udtopia.Value;

import static java.lang.Long.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class PureLongTest
{
	private static final long[] _VALUES = {MIN_VALUE, MIN_VALUE + 1, -1, 0, 1, 123, MAX_VALUE - 1, MAX_VALUE};

	static final @Value class Count extends PureLong<Count>
	{
		Count(final long rawValue) { super(Count::new, rawValue); }

		static Count parse(final String str) { return parse(Count::new, str); }
	}

	@Test public void shouldReturnRawValue()
	{
		final Count x = new Count(123L);
		assertThat(x.getAsInt(), is(123));
		assertThat(x.getAsLong(), is(123L));
		assertThat(x.getAsDouble(), is(123.0));
		assertThat(x.roundToInt(), is(123));
	}

	@Test(expected = ArithmeticException.class) public void shouldTrapNegativeOverflowWhenGetAsInt()
	{
		new Count((long) Integer.MIN_VALUE - 1L).getAsInt();
	}

	@Test(expected = ArithmeticException.class) public void shouldTrapPositiveOverflowWhenGetAsInt()
	{
		new Count((long) Integer.MAX_VALUE + 1L).getAsInt();
	}

	@Test public void shouldRoundToNearestInt()
	{
		final Count x = new Count(Integer.MIN_VALUE - 1L);
		assertThat(x.roundToInt(), is(Integer.MIN_VALUE));

		final Count y = new Count(Integer.MAX_VALUE + 1L);
		assertThat(y.roundToInt(), is(Integer.MAX_VALUE));
	}

	@Test public void shouldReturnRawValueAfterConstruction()
	{
		for (final long n: _VALUES)
		{
			assertThat(new Count(n).getAsLong(), equalTo(n));
			assertThat(new Count(n).getAsLong(), equalTo(n));
			assertThat(new Count(n).getAsDouble(), equalTo((double) n));
		}
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void shouldAlwaysBeUnequalToNull()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			assertThat(x.equals(null), is(false));
		}
	}

	static final @Value class Count2 extends PureLong<Count2>
	{
		Count2(final long x) { super(Count2::new, x); }
	}

	@Test public void shouldAlwaysBeUnequalToDifferentClass()
	{
		final Count x = new Count(123);
		final Count2 y = new Count2(123);
		assertThat(x, is(not(equalTo(y))));
	}

	@Test(expected = AssertionError.class) public void shouldTrapNullEq()
	{
		final Count x = new Count(0);
		x.eq(null);
	}

	@Test public void shouldAlwaysBeEqualWithSameRaw()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			final Count y = new Count(n);
			assertThat(x, is(equalTo(y)));
			assertThat(x.equals(y), is(true));
			assertThat(x.eq(y), is(true));
		}
	}

	@Test public void shouldBeReflexive()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			assertThat(x, is(equalTo(x)));
			assertThat(x.eq(x), is(true));
		}
	}

	@Test public void shouldBeSymmetric()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);

			final Count y = new Count(n);
			assertThat(x, is(equalTo(y)));
			assertThat(x.eq(y), is(true));
			assertThat(y, is(equalTo(x)));
			assertThat(y.eq(x), is(true));

			final Count z = new Count(314159);
			assertThat(x, is(not(equalTo(z))));
			assertThat(x.eq(z), is(false));
			assertThat(z, is(not(equalTo(x))));
			assertThat(z.eq(x), is(false));
		}
	}

	@Test public void shouldBeTransitive()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			final Count y = new Count(n);
			final Count z = new Count(n);
			assertThat(x, is(equalTo(y)));
			assertThat(x.eq(y), is(true));
			assertThat(y, is(equalTo(z)));
			assertThat(y.eq(z), is(true));
			assertThat(x, is(equalTo(z)));
			assertThat(x.eq(z), is(true));

			final Count w = new Count(314159);
			assertThat(w, is(not(equalTo(y))));
			assertThat(w.eq(y), is(false));
			assertThat(w, is(not(equalTo(z))));
			assertThat(w.eq(z), is(false));
		}
	}

	@Test public void shouldBeConsistent()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			final Count y = new Count(n);
			final Count z = new Count(314159);
			assertThat(x, is(equalTo(y)));
			assertThat(x.eq(y), is(true));
			assertThat(x, is(not(equalTo(z))));
			assertThat(x.eq(z), is(false));
			assertThat(x, is(equalTo(y)));
			assertThat(x.eq(y), is(true));
			assertThat(x, is(not(equalTo(z))));
			assertThat(x.eq(z), is(false));

			final int xHash1 = x.hashCode();
			final int xHash2 = x.hashCode();
			assertThat(xHash1, equalTo(xHash2));
		}
	}

	@Test public void shouldComputeSameHashCode()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			final Count y = new Count(n);
			assertThat(x, is(equalTo(y)));
			assertThat(x.eq(y), is(true));

			final int xHash = x.hashCode();
			final int yHash = y.hashCode();
			assertThat(xHash, equalTo(yHash));
		}
	}

	@Test public void shouldComputeDifferentHashCodes()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			for (final long o: _VALUES)
			{
				final Count y = new Count(o);
				if (n != o && n + 1 != -o)
				{
					final int xHash = x.hashCode();
					final int yHash = y.hashCode();
					assertThat(xHash, is(not(equalTo(yHash))));
				}
			}
		}
	}

	@Test public void shouldGenerateSameStringAsUnderlying()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			assertThat(x.toString(), equalTo(Long.toString(x.getAsLong())));
		}
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

	@Test public void shouldCompareAsLarger()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			for (final long o: _VALUES)
			{
				final Count y = new Count(o);
				if (n > o)
				{
					assertThat(x, is(greaterThan(y)));
					assertThat(x.compareTo(y), is(greaterThan(0)));
					assertThat(x.compareTo(o), is(greaterThan(0)));
					assertThat(x.compareTo(new Count2(o)), is(greaterThan(0)));
				}
			}
		}
	}

	@Test public void shouldCompareAsSmaller()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			for (final long o: _VALUES)
			{
				final Count y = new Count(o);
				if (n < o)
				{
					assertThat(x, is(lessThan(y)));
					assertThat(x.compareTo(y), is(lessThan(0)));
					assertThat(x.compareTo(o), is(lessThan(0)));
					assertThat(x.compareTo(new Count2(o)), is(lessThan(0)));
				}
			}
		}
	}

	@Test public void shouldCompareAsEqual()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			for (final long o: _VALUES)
			{
				final Count y = new Count(o);
				if (n == o)
				{
					assertThat(x, is(equalTo(y)));
					assertThat(x.compareTo(y), is(equalTo(0)));
					assertThat(x.compareTo(o), is(equalTo(0)));
					assertThat(x.compareTo(new Count2(o)), is(equalTo(0)));
				}
			}
		}
	}

	@Test public void shouldBeGreaterThan()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			for (final long o: _VALUES)
			{
				final Count y = new Count(o);
				if (n > o)
				{
					assertThat(x.isGreaterThan(y), is(true));
					assertThat(x.isGreaterThan(o), is(true));
				}
			}
		}
	}

	@Test public void shouldNotBeGreaterThan()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			for (final long o: _VALUES)
			{
				final Count y = new Count(o);
				if (n <= o)
				{
					assertThat(x.isGreaterThan(y), is(false));
					assertThat(x.isGreaterThan(o), is(false));
				}
			}
		}
	}

	@Test public void shouldBeGreaterThanOrEqualTo()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			for (final long o: _VALUES)
			{
				final Count y = new Count(o);
				if (n >= o)
				{
					assertThat(x.isGreaterThanOrEqualTo(y), is(true));
					assertThat(x.isGreaterThanOrEqualTo(o), is(true));
				}
			}
		}
	}

	@Test public void shouldNotBeGreaterThanOrEqualTo()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			for (final long o: _VALUES)
			{
				final Count y = new Count(o);
				if (n < o)
				{
					assertThat(x.isGreaterThanOrEqualTo(y), is(false));
					assertThat(x.isGreaterThanOrEqualTo(o), is(false));
				}
			}
		}
	}

	@Test public void shouldBeLessThan()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			for (final long o: _VALUES)
			{
				final Count y = new Count(o);
				if (n < o)
				{
					assertThat(x.isLessThan(y), is(true));
					assertThat(x.isLessThan(o), is(true));
				}
			}
		}
	}

	@Test public void shouldNotBeLessThan()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			for (final long o: _VALUES)
			{
				final Count y = new Count(o);
				if (n >= o)
				{
					assertThat(x.isLessThan(y), is(false));
					assertThat(x.isLessThan(o), is(false));
				}
			}
		}
	}

	@Test public void shouldBeLessThanOrEqualTo()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			for (final long o: _VALUES)
			{
				final Count y = new Count(o);
				if (n <= o)
				{
					assertThat(x.isLessThanOrEqualTo(y), is(true));
					assertThat(x.isLessThanOrEqualTo(o), is(true));
				}
			}
		}
	}

	@Test public void shouldNotBeLessThanOrEqualTo()
	{
		for (final long n: _VALUES)
		{
			final Count x = new Count(n);
			for (final long o: _VALUES)
			{
				final Count y = new Count(o);
				if (n > o)
				{
					assertThat(x.isLessThanOrEqualTo(y), is(false));
					assertThat(x.isLessThanOrEqualTo(o), is(false));
				}
			}
		}
	}

	@Test public void shouldCompareToSupplier()
	{
		final Count x = new Count(5);

		assertThat(x.isGreaterThan(new Count2(3)), is(true));
		assertThat(x.isGreaterThan(new Count2(5)), is(false));
		assertThat(x.isGreaterThan(new Count2(6)), is(false));

		assertThat(x.isGreaterThanOrEqualTo(new Count2(3)), is(true));
		assertThat(x.isGreaterThanOrEqualTo(new Count2(5)), is(true));
		assertThat(x.isGreaterThanOrEqualTo(new Count2(6)), is(false));

		assertThat(x.isLessThan(new Count2(3)), is(false));
		assertThat(x.isLessThan(new Count2(5)), is(false));
		assertThat(x.isLessThan(new Count2(6)), is(true));

		assertThat(x.isLessThanOrEqualTo(new Count2(3)), is(false));
		assertThat(x.isLessThanOrEqualTo(new Count2(5)), is(true));
		assertThat(x.isLessThanOrEqualTo(new Count2(6)), is(true));
	}

	@Test public void shouldMapRawValue()
	{
		final Count x = new Count(2);
		final Count y = x.map(v -> v * 3);
		assertThat(y.getAsLong(), is(6L));
	}

	@Test public void shouldMapToIdenticalInstance()
	{
		final Count x = new Count(2);
		@SuppressWarnings("PointlessArithmeticExpression") final Count y = x.map(v -> v * 1);
		assertThat(y, is(sameInstance(x)));
	}

	@Test public void shouldConvertToAnotherPure()
	{
		final Count x = new Count(2);
		final Count2 y = x.map(c -> c + 1, Count2::new);

		final Count2 z = x.getAs(Count2::new);
		assertThat(z.getAsLong(), is(equalTo(2L)));
	}

	@Test public void shouldMatchCondition()
	{
		final Count x = new Count(2);
		final Count y = new Count(5);
		assertThat(x.is(d -> d < 3), is(true));
		assertThat(y.is(d -> d < 3), is(false));
	}

	@Test public void shouldNotMatchCondition()
	{
		final Count x = new Count(2);
		final Count y = new Count(5);
		assertThat(x.isNot(d -> d > 3), is(true));
		assertThat(y.isNot(d -> d > 3), is(false));
	}

	@Test public void shouldBeZero()
	{
		final Count x = new Count(0);
		assertThat(x.isZero(), is(true));
		assertThat(x.isNonZero(), is(false));
		assertThat(x.isPositive(), is(false));
		assertThat(x.isNegative(), is(false));
	}

	@Test public void shouldNotBeZero()
	{
		final Count x = new Count(1);
		assertThat(x.isZero(), is(false));
		assertThat(x.isNonZero(), is(true));
		assertThat(x.isNegative(), is(false));
		assertThat(x.isPositive(), is(true));

		final Count y = new Count(-1);
		assertThat(y.isZero(), is(false));
		assertThat(y.isNonZero(), is(true));
		assertThat(y.isNegative(), is(true));
		assertThat(y.isPositive(), is(false));
	}

	@Test public void shouldFormatPrimitive()
	{
		final Count x = new Count(1234);
		final String s = x.format(new DecimalFormat("#,##0.000 times"));
		assertThat(s, is("1,234.000 times"));
	}
}
