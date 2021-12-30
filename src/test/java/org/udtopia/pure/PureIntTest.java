package org.udtopia.pure;

import java.text.DecimalFormat;
import org.junit.Test;
import org.udtopia.UDTInt;
import org.udtopia.Value;

import static java.lang.Integer.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class PureIntTest
{
	private static final int[] _VALUES = {MIN_VALUE, MIN_VALUE + 1, -1, 0, 1, 123, MAX_VALUE - 1, MAX_VALUE};

	static final @Value class Count extends PureInt<Count>
	{
		Count(final int rawValue) { super(Count::new, rawValue); }

		static Count parse(final String str) { return parse(Count::new, str); }
	}

	@Test public void shouldReturnIntegerLimits()
	{
		assertThat(UDTInt.nearestInt(MIN_VALUE - 1L), is(MIN_VALUE));
		assertThat(UDTInt.nearestInt(MIN_VALUE), is(MIN_VALUE));
		assertThat(UDTInt.nearestInt(MIN_VALUE + 1L), is(MIN_VALUE + 1));

		assertThat(UDTInt.nearestInt(MAX_VALUE - 1L), is(MAX_VALUE - 1));
		assertThat(UDTInt.nearestInt(MAX_VALUE), is(MAX_VALUE));
		assertThat(UDTInt.nearestInt(MAX_VALUE + 1L), is(MAX_VALUE));
	}

	@Test public void shouldReturnRawValue()
	{
		final Count x = new Count(123);
		assertThat(x.getAsInt(), is(123));
		assertThat(x.getAsLong(), is(123L));
		assertThat(x.getAsDouble(), is(123.0));
	}

	@Test public void shouldReturnRawValueAfterConstruction()
	{
		for (final int n: _VALUES)
		{
			assertThat(new Count(n).getAsInt(), equalTo(n));
			assertThat(new Count(n).getAsInt(), equalTo(n));
			assertThat(new Count(n).getAsLong(), equalTo((long) n));
			assertThat(new Count(n).getAsDouble(), equalTo((double) n));
		}
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void shouldAlwaysBeUnequalToNull()
	{
		for (final int n: _VALUES)
		{
			final Count x = new Count(n);
			assertThat(x.equals(null), is(false));
		}
	}

	static final @Value class Count2 extends PureInt<Count2>
	{
		Count2(final int x) { super(Count2::new, x); }
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
		for (final int n: _VALUES)
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
		for (final int n: _VALUES)
		{
			final Count x = new Count(n);
			assertThat(x, is(equalTo(x)));
			assertThat(x.eq(x), is(true));
		}
	}

	@Test public void shouldBeSymmetric()
	{
		for (final int n: _VALUES)
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
		for (final int n: _VALUES)
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
		for (final int n: _VALUES)
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
		for (final int n: _VALUES)
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
		for (final int n: _VALUES)
		{
			final Count x = new Count(n);
			for (final int o: _VALUES)
			{
				final Count y = new Count(o);
				if (n != o)
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
		for (final int n: _VALUES)
		{
			final Count x = new Count(n);
			assertThat(x.toString(), equalTo(Integer.toString(x.getAsInt())));
		}
	}

	@Test public void shouldParseOwnToStringOutput()
	{
		for (final int n: _VALUES)
		{
			final Count x = new Count(n);
			final String s = x.toString();
			assertThat(Count.parse(s), is(x));
		}
	}

	@Test public void shouldCompareAsLarger()
	{
		for (final int n: _VALUES)
		{
			final Count x = new Count(n);
			for (final int o: _VALUES)
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
		for (final int n: _VALUES)
		{
			final Count x = new Count(n);
			for (final int o: _VALUES)
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
		for (final int n: _VALUES)
		{
			final Count x = new Count(n);
			for (final int o: _VALUES)
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
		for (final int n: _VALUES)
		{
			final Count x = new Count(n);
			for (final int o: _VALUES)
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
		for (final int n: _VALUES)
		{
			final Count x = new Count(n);
			for (final int o: _VALUES)
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
		for (final int n: _VALUES)
		{
			final Count x = new Count(n);
			for (final int o: _VALUES)
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
		for (final int n: _VALUES)
		{
			final Count x = new Count(n);
			for (final int o: _VALUES)
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
		for (final int n: _VALUES)
		{
			final Count x = new Count(n);
			for (final int o: _VALUES)
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
		for (final int n: _VALUES)
		{
			final Count x = new Count(n);
			for (final int o: _VALUES)
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
		for (final int n: _VALUES)
		{
			final Count x = new Count(n);
			for (final int o: _VALUES)
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
		for (final int n: _VALUES)
		{
			final Count x = new Count(n);
			for (final int o: _VALUES)
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
		assertThat(y.getAsInt(), is(6));
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
		assertThat(y.getAsInt(), is(equalTo(3)));

		final Count2 z = x.getAs(Count2::new);
		assertThat(z.getAsInt(), is(equalTo(2)));
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

	@Test public void shouldFlipPositiveToNegative()
	{
		final Count x = new Count(1);
		assertThat(x.negate(), is(new Count(-1)));
	}

	@Test public void shouldFlipNegativeToPositive()
	{
		final Count x = new Count(-1);
		assertThat(x.negate(), is(new Count(1)));
	}

	@Test public void shouldNotNegateZero()
	{
		final Count x = new Count(0);
		assertThat(x.negate(), is(sameInstance(x)));
	}

	@Test public void shouldFormatPrimitive()
	{
		final Count x = new Count(1234);
		final String s = x.format(new DecimalFormat("#,##0.000 times"));
		assertThat(s, is("1,234.000 times"));
	}

	@Test public void shouldAddZeroAndReturnSame()
	{
		final Count x = new Count(2);
		assertThat(x.add(0), is(sameInstance(x)));
		assertThat(x.add(new Count2(0)), is(sameInstance(x)));
		assertThat(x.add(new Count(0), Count2::new), is(new Count2(2)));
	}

	@Test public void shouldAddNonZero()
	{
		final Count x = new Count(2);
		assertThat(x.add(3), is(new Count(5)));
		assertThat(x.add(new Count2(3)), is(new Count(5)));
		assertThat(x.add(new Count(3), Count2::new), is(new Count2(5)));
	}

	@Test public void shouldAddNegative()
	{
		final Count x = new Count(2);
		assertThat(x.add(-3), is(new Count(-1)));
		assertThat(x.add(new Count2(-3)), is(new Count(-1)));
		assertThat(x.add(new Count(-3), Count2::new), is(new Count2(-1)));
	}

	@Test public void shouldSubtractZeroAndReturnSame()
	{
		final Count x = new Count(2);
		assertThat(x.subtract(0), is(sameInstance(x)));
		assertThat(x.subtract(new Count2(0)), is(sameInstance(x)));
		assertThat(x.subtract(new Count(0), Count2::new), is(new Count2(2)));
	}

	@Test public void shouldSubtractNonZero()
	{
		final Count x = new Count(5);
		assertThat(x.subtract(3), is(new Count(2)));
		assertThat(x.subtract(new Count2(3)), is(new Count(2)));
		assertThat(x.subtract(new Count(3), Count2::new), is(new Count2(2)));
	}

	@Test public void shouldSubtractNegative()
	{
		final Count x = new Count(-3);
		assertThat(x.subtract(-5), is(new Count(2)));
		assertThat(x.subtract(new Count2(-5)), is(new Count(2)));
		assertThat(x.subtract(new Count(-5), Count2::new), is(new Count2(2)));
	}

	@Test public void shouldSubtractNonZeroFromThis()
	{
		final Count x = new Count(5);
		assertThat(x.subtractFrom(3), is(new Count(-2)));
		assertThat(x.subtractFrom(new Count2(3)), is(new Count(-2)));
		assertThat(x.subtractFrom(new Count(3), Count2::new), is(new Count2(-2)));
	}

	@Test public void shouldSubtractNegativeFromThis()
	{
		final Count x = new Count(-3);
		assertThat(x.subtractFrom(-5), is(new Count(-2)));
		assertThat(x.subtractFrom(new Count2(-5)), is(new Count(-2)));
		assertThat(x.subtractFrom(new Count(-5), Count2::new), is(new Count2(-2)));
	}

	@Test public void shouldMultiplyByOneAndReturnSame()
	{
		final Count x = new Count(3);
		assertThat(x.multiplyBy(1), is(sameInstance(x)));
		assertThat(x.multiplyBy(new Count2(1)), is(sameInstance(x)));
		assertThat(x.multiplyBy(new Count(1), Count2::new), is(new Count2(3)));
	}

	@Test public void shouldMultiplyByZero()
	{
		final Count x = new Count(2);
		assertThat(x.multiplyBy(0), is(new Count(0)));
		assertThat(x.multiplyBy(new Count2(0)), is(new Count(0)));
		assertThat(x.multiplyBy(new Count(0), Count2::new), is(new Count2(0)));
	}

	@Test public void shouldMultiplyByNonOne()
	{
		final Count x = new Count(2);
		assertThat(x.multiplyBy(4), is(new Count(8)));
		assertThat(x.multiplyBy(new Count2(4)), is(new Count(8)));
		assertThat(x.multiplyBy(new Count(4), Count2::new), is(new Count2(8)));
	}

	@Test public void shouldMultiplyByNegative()
	{
		final Count x = new Count(2);
		assertThat(x.multiplyBy(-3), is(new Count(-6)));
		assertThat(x.multiplyBy(new Count2(-3)), is(new Count(-6)));
		assertThat(x.multiplyBy(new Count(-3), Count2::new), is(new Count2(-6)));
	}

	@Test public void shouldDivideByOneAndReturnSame()
	{
		final Count x = new Count(4);
		assertThat(x.divideBy(1), is(sameInstance(x)));
		assertThat(x.divideBy(new Count2(1)), is(sameInstance(x)));
		assertThat(x.divideBy(new Count(1), Count2::new), is(new Count2(4)));
	}

	@Test public void shouldDivideByFactor()
	{
		final Count x = new Count(12);
		assertThat(x.divideBy(4), is(new Count(3)));
		assertThat(x.divideBy(new Count2(4)), is(new Count(3)));
		assertThat(x.divideBy(new Count(4), Count2::new), is(new Count2(3)));
	}

	@Test public void shouldDivideByNonFactorAndRoundDown()
	{
		final Count x = new Count(12);
		assertThat(x.divideBy(5), is(new Count(2)));
		assertThat(x.divideBy(new Count2(5)), is(new Count(2)));
		assertThat(x.divideBy(new Count(5), Count2::new), is(new Count2(2)));
	}

	@Test public void shouldDivideMultiple()
	{
		final Count x = new Count(4);
		assertThat(x.divide(12), is(new Count(3)));
		assertThat(x.divide(new Count2(12)), is(new Count(3)));
		assertThat(x.divide(new Count(12), Count2::new), is(new Count2(3)));
	}

	@Test public void shouldDivideNonMultipleAndRoundDown()
	{
		final Count x = new Count(5);
		assertThat(x.divide(12), is(new Count(2)));
		assertThat(x.divide(new Count2(12)), is(new Count(2)));
		assertThat(x.divide(new Count(12), Count2::new), is(new Count2(2)));
	}
}
