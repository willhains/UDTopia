package org.udtopia.pure;

import org.junit.Test;
import org.udtopia.Value;

import static java.lang.Double.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class PureDoubleTest
{
	private static final double[] _VALUES = {
		NEGATIVE_INFINITY, -MAX_VALUE, -1.5, -MIN_VALUE, -0.0,
		0.0, MIN_VALUE, 0.1, 123.0, MAX_VALUE, POSITIVE_INFINITY};

	static final @Value class Height extends PureDouble<Height>
	{
		Height(final double rawValue) { super(Height::new, rawValue); }

		static Height parse(final String str) { return parse(Height::new, str); }
	}

	@Test public void shouldReturnRawValue()
	{
		final Height x = new Height(123.0);
		assertThat(x.getAsInt(), is(123));
		assertThat(x.getAsLong(), is(123L));
		assertThat(x.getAsDouble(), is(123.0));
		assertThat(x.roundToInt(), is(123));
		assertThat(x.roundToLong(), is(123L));
	}

	@Test(expected = ArithmeticException.class) public void shouldTrapLossOfPrecisionWhenGetAsInt()
	{
		new Height(123.4).getAsInt();
	}

	@Test(expected = ArithmeticException.class) public void shouldTrapLossOfPrecisionWhenGetAsLong()
	{
		new Height(123.4).getAsLong();
	}

	@Test(expected = ArithmeticException.class) public void shouldTrapNegativeOverflowWhenGetAsInt()
	{
		new Height(Math.nextDown((double) Integer.MIN_VALUE)).getAsInt();
	}

	@Test(expected = ArithmeticException.class) public void shouldTrapNegativeOverflowWhenGetAsLong()
	{
		new Height(Math.nextDown((double) Long.MIN_VALUE)).getAsLong();
	}

	@Test(expected = ArithmeticException.class) public void shouldTrapPositiveOverflowWhenGetAsInt()
	{
		new Height(Math.nextUp((double) Integer.MAX_VALUE)).getAsInt();
	}

	@Test(expected = ArithmeticException.class) public void shouldTrapPositiveOverflowWhenGetAsLong()
	{
		new Height(Math.nextUp((double) Long.MAX_VALUE)).getAsLong();
	}

	@Test public void shouldRoundToNearestInt()
	{
		final Height x = new Height(Integer.MIN_VALUE - 1.5);
		assertThat(x.roundToInt(), is(Integer.MIN_VALUE));

		final Height y = new Height(Integer.MAX_VALUE + 1.5);
		assertThat(y.roundToInt(), is(Integer.MAX_VALUE));
	}

	@Test public void shouldRoundToNearestLong()
	{
		final Height x = new Height(Long.MIN_VALUE - 1.5);
		assertThat(x.roundToLong(), is(Long.MIN_VALUE));

		final Height y = new Height(Long.MAX_VALUE + 1.5);
		assertThat(y.roundToLong(), is(Long.MAX_VALUE));
	}

	@Test public void shouldReturnRawValueAfterConstruction()
	{
		for (final double d: _VALUES)
		{
			assertThat(new Height(d).getAsDouble(), is(d));
			assertThat(new Height(d).getAsDouble(), is(d));
		}
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void shouldAlwaysBeUnequalToNull()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			assertThat(x.equals(null), is(false));
		}
	}

	static final @Value class Height2 extends PureDouble<Height2>
	{
		Height2(final double x) { super(Height2::new, x); }
	}

	@Test public void shouldAlwaysBeUnequalToDifferentClass()
	{
		final Height x = new Height(123.0);
		final Height2 y = new Height2(123.0);
		assertThat(x, is(not(equalTo(y))));
	}

	@Test(expected = AssertionError.class) public void shouldTrapNullEq()
	{
		final Height x = new Height(0);
		x.eq(null);
	}

	@Test public void shouldAlwaysBeEqualWithSameRaw()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			final Height y = new Height(d);
			assertThat(x, is(equalTo(y)));
			assertThat(x.equals(y), is(true));
			assertThat(x.eq(y), is(true));
		}
	}

	@Test public void shouldBeReflexive()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			assertThat(x, is(equalTo(x)));
			assertThat(x.eq(x), is(true));
		}
	}

	@Test public void shouldBeSymmetric()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);

			final Height y = new Height(d);
			assertThat(x, is(equalTo(y)));
			assertThat(x.eq(y), is(true));
			assertThat(y, is(equalTo(x)));
			assertThat(y.eq(x), is(true));

			final Height z = new Height(Math.PI);
			assertThat(x, is(not(equalTo(z))));
			assertThat(x.eq(z), is(false));
			assertThat(z, is(not(equalTo(x))));
			assertThat(z.eq(x), is(false));
		}
	}

	@Test public void shouldBeTransitive()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			final Height y = new Height(d);
			final Height z = new Height(d);
			assertThat(x, is(equalTo(y)));
			assertThat(x.eq(y), is(true));
			assertThat(y, is(equalTo(z)));
			assertThat(y.eq(z), is(true));
			assertThat(x, is(equalTo(z)));
			assertThat(x.eq(z), is(true));

			final Height w = new Height(Math.PI);
			assertThat(w, is(not(equalTo(y))));
			assertThat(w.eq(y), is(false));
			assertThat(w, is(not(equalTo(z))));
			assertThat(w.eq(z), is(false));
		}
	}

	@Test public void shouldBeConsistent()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			final Height y = new Height(d);
			final Height z = new Height(Math.PI);
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
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			final Height y = new Height(d);
			assertThat(x, is(equalTo(y)));
			assertThat(x.eq(y), is(true));

			final int xHash = x.hashCode();
			final int yHash = y.hashCode();
			assertThat(xHash, equalTo(yHash));
		}
	}

	@Test public void shouldComputeDifferentHashCodes()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			for (final double e: _VALUES)
			{
				final Height y = new Height(e);
				if (d != e)
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
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			assertThat(x.toString(), equalTo(Double.toString(x.getAsDouble())));
		}
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

	@Test public void shouldCompareAsLarger()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			for (final double e: _VALUES)
			{
				final Height y = new Height(e);
				if (d > e)
				{
					assertThat(x, is(greaterThan(y)));
					assertThat(x.compareTo(y), is(greaterThan(0)));
					assertThat(x.compareTo(e), is(greaterThan(0)));
					assertThat(x.compareTo(new Height2(e)), is(greaterThan(0)));
				}
			}
		}
	}

	@Test public void shouldCompareAsSmaller()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			for (final double e: _VALUES)
			{
				final Height y = new Height(e);
				if (d < e)
				{
					assertThat(x, is(lessThan(y)));
					assertThat(x.compareTo(y), is(lessThan(0)));
					assertThat(x.compareTo(e), is(lessThan(0)));
					assertThat(x.compareTo(new Height2(e)), is(lessThan(0)));
				}
			}
		}
	}

	@Test public void shouldCompareAsEqual()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			for (final double e: _VALUES)
			{
				final Height y = new Height(e);
				if (compare(d, e) == 0) // +ve zero == -ve zero
				{
					assertThat(x, is(equalTo(y)));
					assertThat(x.compareTo(y), is(equalTo(0)));
					assertThat(x.compareTo(e), is(equalTo(0)));
					assertThat(x.compareTo(new Height2(e)), is(equalTo(0)));
				}
			}
		}
	}

	@Test public void shouldBeGreaterThan()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			for (final double e: _VALUES)
			{
				final Height y = new Height(e);
				if (d > e)
				{
					assertThat(x.isGreaterThan(y), is(true));
					assertThat(x.isGreaterThan(e), is(true));
				}
			}
		}
	}

	@Test public void shouldNotBeGreaterThan()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			for (final double e: _VALUES)
			{
				final Height y = new Height(e);
				if (compare(d, e) <= 0) // +ve zero == -ve zero
				{
					assertThat(x.isGreaterThan(y), is(false));
					assertThat(x.isGreaterThan(e), is(false));
				}
			}
		}
	}

	@Test public void shouldBeGreaterThanOrEqualTo()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			for (final double e: _VALUES)
			{
				final Height y = new Height(e);
				if (compare(d, e) >= 0) // +ve zero == -ve zero
				{
					assertThat(x.isGreaterThanOrEqualTo(y), is(true));
					assertThat(x.isGreaterThanOrEqualTo(e), is(true));
				}
			}
		}
	}

	@Test public void shouldNotBeGreaterThanOrEqualTo()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			for (final double e: _VALUES)
			{
				final Height y = new Height(e);
				if (d < e)
				{
					assertThat(x.isGreaterThanOrEqualTo(y), is(false));
					assertThat(x.isGreaterThanOrEqualTo(e), is(false));
				}
			}
		}
	}

	@Test public void shouldBeLessThan()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			for (final double e: _VALUES)
			{
				final Height y = new Height(e);
				if (d < e)
				{
					assertThat(x.isLessThan(y), is(true));
					assertThat(x.isLessThan(e), is(true));
				}
			}
		}
	}

	@Test public void shouldNotBeLessThan()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			for (final double e: _VALUES)
			{
				final Height y = new Height(e);
				if (compare(d, e) >= 0) // +ve zero == -ve zero
				{
					assertThat(x.isLessThan(y), is(false));
					assertThat(x.isLessThan(e), is(false));
				}
			}
		}
	}

	@Test public void shouldBeLessThanOrEqualTo()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			for (final double e: _VALUES)
			{
				final Height y = new Height(e);
				if (compare(d, e) <= 0) // +ve zero == -ve zero
				{
					assertThat(x.isLessThanOrEqualTo(y), is(true));
					assertThat(x.isLessThanOrEqualTo(e), is(true));
				}
			}
		}
	}

	@Test public void shouldNotBeLessThanOrEqualTo()
	{
		for (final double d: _VALUES)
		{
			final Height x = new Height(d);
			for (final double e: _VALUES)
			{
				final Height y = new Height(e);
				if (d > e)
				{
					assertThat(x.isLessThanOrEqualTo(y), is(false));
					assertThat(x.isLessThanOrEqualTo(e), is(false));
				}
			}
		}
	}

	@Test public void shouldCompareToSupplier()
	{
		final Height x = new Height(5.0);

		assertThat(x.isGreaterThan(new Height2(3.0)), is(true));
		assertThat(x.isGreaterThan(new Height2(5.0)), is(false));
		assertThat(x.isGreaterThan(new Height2(5.1)), is(false));

		assertThat(x.isGreaterThanOrEqualTo(new Height2(3.0)), is(true));
		assertThat(x.isGreaterThanOrEqualTo(new Height2(5.0)), is(true));
		assertThat(x.isGreaterThanOrEqualTo(new Height2(5.1)), is(false));

		assertThat(x.isLessThan(new Height2(3.0)), is(false));
		assertThat(x.isLessThan(new Height2(5.0)), is(false));
		assertThat(x.isLessThan(new Height2(5.1)), is(true));

		assertThat(x.isLessThanOrEqualTo(new Height2(3.0)), is(false));
		assertThat(x.isLessThanOrEqualTo(new Height2(5.0)), is(true));
		assertThat(x.isLessThanOrEqualTo(new Height2(5.1)), is(true));
	}

	@Test public void shouldMapRawValue()
	{
		final Height x = new Height(2.0);
		final Height y = x.map(v -> v * 3.5);
		assertThat(y.getAsDouble(), is(7.0));
	}

	@Test public void shouldMapToIdenticalInstance()
	{
		final Height x = new Height(2.0);
		@SuppressWarnings("PointlessArithmeticExpression") final Height y = x.map(v -> v * 1.0);
		assertThat(y, is(sameInstance(x)));
	}

	@Test public void shouldConvertToAnotherPure()
	{
		final Height x = new Height(2.0);
		final Height2 y = x.map(h -> h * 100.0, Height2::new);
		assertThat(y.getAsDouble(), is(equalTo(200.0)));

		final Height2 z = x.getAs(Height2::new);
		assertThat(z.getAsDouble(), is(equalTo(2.0)));
	}

	@Test public void shouldMatchCondition()
	{
		final Height x = new Height(2.0);
		final Height y = new Height(5.4);
		assertThat(x.is(d -> d < 3.0), is(true));
		assertThat(y.is(d -> d < 3.0), is(false));
	}

	@Test public void shouldNotMatchCondition()
	{
		final Height x = new Height(2.0);
		final Height y = new Height(5.4);
		assertThat(x.isNot(d -> d > 3.0), is(true));
		assertThat(y.isNot(d -> d > 3.0), is(false));
	}
}
