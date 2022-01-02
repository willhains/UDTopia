package org.udtopia.pure;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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

	@Test public void shouldBeZero()
	{
		final Height x = new Height(0.0);
		assertThat(x.isZero(), is(true));
		assertThat(x.isNonZero(), is(false));
		assertThat(x.isPositive(), is(false));
		assertThat(x.isNegative(), is(false));

		final Height y = new Height(-0.0);
		assertThat(y.isZero(), is(true));
		assertThat(y.isNonZero(), is(false));
		assertThat(y.isPositive(), is(false));
		assertThat(y.isNegative(), is(false));
	}

	@Test public void shouldNotBeZero()
	{
		final Height x = new Height(1);
		assertThat(x.isZero(), is(false));
		assertThat(x.isNonZero(), is(true));
		assertThat(x.isNegative(), is(false));
		assertThat(x.isPositive(), is(true));

		final Height y = new Height(-1);
		assertThat(y.isZero(), is(false));
		assertThat(y.isNonZero(), is(true));
		assertThat(y.isNegative(), is(true));
		assertThat(y.isPositive(), is(false));
	}

	@Test public void shouldFormatPrimitive()
	{
		final Height x = new Height(1234.56789);
		final String s = x.format(new DecimalFormat("#,##0.000 m"));
		assertThat(s, is("1,234.568 m"));
	}

	@Test public void shouldFormatWithCustomPattern()
	{
		final String pattern = "#,##0.00";
		final NumberFormat formatter = new DecimalFormat(pattern);
		final Height x = new Height(1.4325);
		assertThat(x.format(pattern), is(equalTo(x.format(formatter))));
	}

	@Test public void shouldAddZeroAndReturnSame()
	{
		final Height x = new Height(2.0);
		assertThat(x.add(0.0), is(sameInstance(x)));
		assertThat(x.add(new Height2(0.0)), is(sameInstance(x)));
		assertThat(x.add(new PureLongTest.Count(0)), is(sameInstance(x)));
		assertThat(x.add(new PureIntTest.Count(0)), is(sameInstance(x)));
		assertThat(x.add(new Height(0.0), Height2::new), is(new Height2(2.0)));
	}

	@Test public void shouldAddNonZero()
	{
		final Height x = new Height(2.0);
		assertThat(x.add(3.5).getAsDouble(), is(5.5));
		assertThat(x.add(new Height2(3.5)).getAsDouble(), is(5.5));
		assertThat(x.add(new PureLongTest.Count(3)).getAsDouble(), is(5.0));
		assertThat(x.add(new PureIntTest.Count(3)).getAsDouble(), is(5.0));
		assertThat(x.add(new Height(3.5), Height2::new), is(new Height2(5.5)));
	}

	@Test public void shouldAddNegative()
	{
		final Height x = new Height(2.0);
		assertThat(x.add(-3.5).getAsDouble(), is(-1.5));
		assertThat(x.add(new Height2(-3.5)).getAsDouble(), is(-1.5));
		assertThat(x.add(new PureLongTest.Count(-3)).getAsDouble(), is(-1.0));
		assertThat(x.add(new PureIntTest.Count(-3)).getAsDouble(), is(-1.0));
		assertThat(x.add(new Height(-3.5), Height2::new), is(new Height2(-1.5)));
	}

	@Test public void shouldSubtractZeroAndReturnSame()
	{
		final Height x = new Height(2.0);
		assertThat(x.subtract(0.0), is(sameInstance(x)));
		assertThat(x.subtract(new Height2(0.0)), is(sameInstance(x)));
		assertThat(x.subtract(new PureLongTest.Count(0)), is(sameInstance(x)));
		assertThat(x.subtract(new PureIntTest.Count(0)), is(sameInstance(x)));
		assertThat(x.subtract(new Height(0.0), Height2::new), is(new Height2(2.0)));
	}

	@Test public void shouldSubtractNonZero()
	{
		final Height x = new Height(5.5);
		assertThat(x.subtract(3.5).getAsDouble(), is(2.0));
		assertThat(x.subtract(new Height2(3.5)).getAsDouble(), is(2.0));
		assertThat(x.subtract(new PureLongTest.Count(3)).getAsDouble(), is(2.5));
		assertThat(x.subtract(new PureIntTest.Count(3)).getAsDouble(), is(2.5));
		assertThat(x.subtract(new Height(3.5), Height2::new), is(new Height2(2.0)));
	}

	@Test public void shouldSubtractNegative()
	{
		final Height x = new Height(-3.5);
		assertThat(x.subtract(-5.5).getAsDouble(), is(2.0));
		assertThat(x.subtract(new Height2(-5.5)).getAsDouble(), is(2.0));
		assertThat(x.subtract(new PureLongTest.Count(-5)).getAsDouble(), is(1.5));
		assertThat(x.subtract(new PureIntTest.Count(-5)).getAsDouble(), is(1.5));
		assertThat(x.subtract(new Height(-5.5), Height2::new), is(new Height2(2.0)));
	}

	@Test public void shouldSubtractNonZeroFromThis()
	{
		final Height x = new Height(5.5);
		assertThat(x.subtractFrom(3.5).getAsDouble(), is(-2.0));
		assertThat(x.subtractFrom(new Height2(3.5)).getAsDouble(), is(-2.0));
		assertThat(x.subtractFrom(new PureLongTest.Count(3)).getAsDouble(), is(-2.5));
		assertThat(x.subtractFrom(new PureIntTest.Count(3)).getAsDouble(), is(-2.5));
		assertThat(x.subtractFrom(new Height(3.5), Height2::new), is(new Height2(-2.0)));
	}

	@Test public void shouldSubtractNegativeFromThis()
	{
		final Height x = new Height(-3.5);
		assertThat(x.subtractFrom(-5.5).getAsDouble(), is(-2.0));
		assertThat(x.subtractFrom(new Height2(-5.5)).getAsDouble(), is(-2.0));
		assertThat(x.subtractFrom(new PureLongTest.Count(-5)).getAsDouble(), is(-1.5));
		assertThat(x.subtractFrom(new PureIntTest.Count(-5)).getAsDouble(), is(-1.5));
		assertThat(x.subtractFrom(new Height(-5.5), Height2::new), is(new Height2(-2.0)));
	}

	@Test public void shouldMultiplyByOneAndReturnSame()
	{
		final Height x = new Height(3.5);
		assertThat(x.multiplyBy(1.0), is(sameInstance(x)));
		assertThat(x.multiplyBy(new Height2(1.0)), is(sameInstance(x)));
		assertThat(x.multiplyBy(new PureLongTest.Count(1)), is(sameInstance(x)));
		assertThat(x.multiplyBy(new PureIntTest.Count(1)), is(sameInstance(x)));
		assertThat(x.multiplyBy(new Height(1.0), Height2::new), is(new Height2(3.5)));
	}

	@Test public void shouldMultiplyByZero()
	{
		final Height x = new Height(2.0);
		assertThat(x.multiplyBy(0.0).getAsDouble(), is(0.0));
		assertThat(x.multiplyBy(new Height2(0.0)).getAsDouble(), is(0.0));
		assertThat(x.multiplyBy(new PureLongTest.Count(0)).getAsDouble(), is(0.0));
		assertThat(x.multiplyBy(new PureIntTest.Count(0)).getAsDouble(), is(0.0));
		assertThat(x.multiplyBy(new Height(0.0), Height2::new), is(new Height2(0.0)));
	}

	@Test public void shouldMultiplyByNonOne()
	{
		final Height x = new Height(2.0);
		assertThat(x.multiplyBy(4.5).getAsDouble(), is(9.0));
		assertThat(x.multiplyBy(new Height2(4.5)).getAsDouble(), is(9.0));
		assertThat(x.multiplyBy(new PureLongTest.Count(4)).getAsDouble(), is(8.0));
		assertThat(x.multiplyBy(new PureIntTest.Count(4)).getAsDouble(), is(8.0));
		assertThat(x.multiplyBy(new Height(4.5), Height2::new), is(new Height2(9.0)));
	}

	@Test public void shouldMultiplyByNegative()
	{
		final Height x = new Height(2.0);
		assertThat(x.multiplyBy(-3.25).getAsDouble(), is(-6.5));
		assertThat(x.multiplyBy(new Height2(-3.25)).getAsDouble(), is(-6.5));
		assertThat(x.multiplyBy(new PureLongTest.Count(-3)).getAsDouble(), is(-6.0));
		assertThat(x.multiplyBy(new PureIntTest.Count(-3)).getAsDouble(), is(-6.0));
		assertThat(x.multiplyBy(new Height(-3.25), Height2::new), is(new Height2(-6.5)));
	}

	@Test public void shouldDivideByOneAndReturnSame()
	{
		final Height x = new Height(4.5);
		assertThat(x.divideBy(1.0), is(sameInstance(x)));
		assertThat(x.divideBy(new Height2(1.0)), is(sameInstance(x)));
		assertThat(x.divideBy(new PureLongTest.Count(1)), is(sameInstance(x)));
		assertThat(x.divideBy(new PureIntTest.Count(1)), is(sameInstance(x)));
		assertThat(x.divideBy(new Height(1.0), Height2::new), is(new Height2(4.5)));
	}

	@Test public void shouldDivideByFactor()
	{
		final Height x = new Height(12.0);
		assertThat(x.divideBy(4.0).getAsDouble(), is(3.0));
		assertThat(x.divideBy(new Height2(4.0)).getAsDouble(), is(3.0));
		assertThat(x.divideBy(new PureLongTest.Count(4)).getAsDouble(), is(3.0));
		assertThat(x.divideBy(new PureIntTest.Count(4)).getAsDouble(), is(3.0));
		assertThat(x.divideBy(new Height(4.0), Height2::new), is(new Height2(3.0)));
	}

	@Test public void shouldDivideByNonFactor()
	{
		final Height x = new Height(12);
		assertThat(x.divideBy(5.0).getAsDouble(), is(2.4));
		assertThat(x.divideBy(new Height2(5.0)).getAsDouble(), is(2.4));
		assertThat(x.divideBy(new PureLongTest.Count(5)).getAsDouble(), is(2.4));
		assertThat(x.divideBy(new PureIntTest.Count(5)).getAsDouble(), is(2.4));
		assertThat(x.divideBy(new Height(5.0), Height2::new), is(new Height2(2.4)));
	}

	@Test public void shouldDivideMultiple()
	{
		final Height x = new Height(4.0);
		assertThat(x.divide(12.0).getAsDouble(), is(3.0));
		assertThat(x.divide(new Height2(12.0)).getAsDouble(), is(3.0));
		assertThat(x.divide(new PureLongTest.Count(12)).getAsDouble(), is(3.0));
		assertThat(x.divide(new PureIntTest.Count(12)).getAsDouble(), is(3.0));
		assertThat(x.divide(new Height(12.0), Height2::new), is(new Height2(3.0)));
	}

	@Test public void shouldDivideNonMultiple()
	{
		final Height x = new Height(5);
		assertThat(x.divide(12.0).getAsDouble(), is(2.4));
		assertThat(x.divide(new Height2(12.0)).getAsDouble(), is(2.4));
		assertThat(x.divide(new PureLongTest.Count(12)).getAsDouble(), is(2.4));
		assertThat(x.divide(new PureIntTest.Count(12)).getAsDouble(), is(2.4));
		assertThat(x.divide(new Height(12.0), Height2::new), is(new Height2(2.4)));
	}
}
