package org.udtopia.pure;

import java.awt.Point;
import java.math.BigDecimal;
import org.junit.Test;
import org.udtopia.Value;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class PureValueTest
{
	static final @Value class Height extends PureValue<BigDecimal, Height>
	{
		Height(final BigDecimal rawValue) { super(rawValue); }
	}

	@Test(expected = AssertionError.class) public void shouldTrapNullUnderlyingValue()
	{
		new Height(null);
	}

	@Test public void shouldReturnRawValueAfterConstruction()
	{
		final Height x = new Height(new BigDecimal("123"));
		assertThat(x.get(), equalTo(new BigDecimal("123")));
		assertThat(x.get(), equalTo(new BigDecimal("123")));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void shouldAlwaysBeUnequalToNull()
	{
		final Height x = new Height(new BigDecimal("123"));
		assertThat(x.equals(null), is(false));
	}

	static final @Value class Height2 extends PureValue<BigDecimal, Height2>
	{
		Height2(final BigDecimal x) { super(x); }
	}

	@Test public void shouldAlwaysBeUnequalToDifferentClass()
	{
		final Height x = new Height(new BigDecimal("123"));
		final Height2 y = new Height2(new BigDecimal("123"));
		assertThat(x, is(not(equalTo(y))));
	}

	@Test(expected = AssertionError.class) public void shouldTrapNullEq()
	{
		final Height x = new Height(BigDecimal.ZERO);
		x.eq(null);
	}

	@Test public void shouldAlwaysBeEqualWithSameRaw()
	{
		final BigDecimal d = new BigDecimal("123");
		final Height x = new Height(d);
		final Height y = new Height(d);
		assertThat(x, is(equalTo(y)));
		assertThat(x.eq(y), is(true));
	}

	@Test public void shouldBeReflexive()
	{
		final Height x = new Height(new BigDecimal("123"));
		assertThat(x, is(equalTo(x)));
		assertThat(x.eq(x), is(true));
	}

	@Test public void shouldBeSymmetric()
	{
		final Height x = new Height(new BigDecimal("100"));

		final Height y = new Height(new BigDecimal("100"));
		assertThat(x, is(equalTo(y)));
		assertThat(x.eq(y), is(true));
		assertThat(y, is(equalTo(x)));
		assertThat(y.eq(x), is(true));

		final Height z = new Height(new BigDecimal("101"));
		assertThat(x, is(not(equalTo(z))));
		assertThat(x.eq(z), is(false));
		assertThat(z, is(not(equalTo(x))));
		assertThat(z.eq(x), is(false));
	}

	@Test public void shouldBeTransitive()
	{
		final Height x = new Height(new BigDecimal("100"));
		final Height y = new Height(new BigDecimal("100"));
		final Height z = new Height(new BigDecimal("100"));
		assertThat(x, is(equalTo(y)));
		assertThat(x.eq(y), is(true));
		assertThat(y, is(equalTo(z)));
		assertThat(y.eq(z), is(true));
		assertThat(x, is(equalTo(z)));
		assertThat(x.eq(z), is(true));

		final Height w = new Height(new BigDecimal("101"));
		assertThat(w, is(not(equalTo(y))));
		assertThat(w.eq(y), is(false));
		assertThat(w, is(not(equalTo(z))));
		assertThat(w.eq(z), is(false));
	}

	@Test public void shouldBeConsistent()
	{
		final Height x = new Height(new BigDecimal("100"));
		final Height y = new Height(new BigDecimal("100"));
		final Height z = new Height(new BigDecimal("101"));
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

	@Test public void shouldHaveSameHashCode()
	{
		final Height x = new Height(new BigDecimal("100"));
		final Height y = new Height(new BigDecimal("100"));
		assertThat(x, is(equalTo(y)));

		final int xHash = x.hashCode();
		final int yHash = y.hashCode();
		assertThat(xHash, equalTo(yHash));
	}

	@Test public void shouldHaveDifferentHashCodes()
	{
		final Height x = new Height(new BigDecimal("100"));
		final Height y = new Height(new BigDecimal("101"));
		assertThat(x, is(not(equalTo(y))));

		final int xHash = x.hashCode();
		final int yHash = y.hashCode();
		assertThat(xHash, not(equalTo(yHash)));
	}

	@Test public void shouldGenerateSameStringAsUnderlying()
	{
		final Height x = new Height(new BigDecimal("100"));
		assertThat(x.toString(), equalTo(x.get().toString()));
	}

	static final @Value class A extends PureValue<int[], A>
	{
		A(final int[] a) { super(a); }
	}

	@Test(expected = AssertionError.class) public void shouldTrapArrayAsRawValue()
	{
		new A(new int[] {1});
	}

	static final @Value class MutableWrapper extends PureValue<Point, MutableWrapper>
	{
		MutableWrapper(final Point rawValue) { super(rawValue, original -> new Point(original.x, original.y)); }
	}

	@Test public void shouldNotMutateOriginal()
	{
		final Point point = new Point(3, 4);
		new MutableWrapper(point).get().x = 5;
		assertThat(point.x, is(3));
	}
}
