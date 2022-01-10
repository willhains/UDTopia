package org.udtopia.recycle;

import java.math.BigDecimal;
import org.junit.Test;
import org.udtopia.Value;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class RecyclableValueTest
{
	@RecycleBinSize(1)
	static final @Value class Height extends RecyclableValue<BigDecimal, Height>
	{
		Height(final BigDecimal rawValue) { super(Height::new, rawValue); }
	}

	@Test(expected = AssertionError.class) public void shouldTrapNullUnderlyingValue()
	{
		new Height(null);
	}

	static final @Value class A extends RecyclableValue<int[], A>
	{
		A(final int[] a) { super(A::new, a); }
	}

	@Test(expected = AssertionError.class) public void shouldTrapArrayAsRawValue()
	{
		new A(new int[] {1});
	}

	@Test public void shouldMarkDiscarded()
	{
		final Height x = new Height(new BigDecimal("12.0"));
		assertThat(x.isDiscarded(), is(false));
		x.discard();
		assertThat(x.isDiscarded(), is(true));
	}

	@Test(expected = AssertionError.class) public void shouldTrapDoubleDiscard()
	{
		final Height x = new Height(new BigDecimal("12.0"));
		x.discard();
		x.discard();
	}

	@Test public void shouldRecycleExistingInstance()
	{
		final Height x = RecyclableValue.recycle(Height.class, Height::new, new BigDecimal("12.0"));
		assertThat(x.get(), is(new BigDecimal("12.0")));
		x.discard();
		final Height y = RecyclableValue.recycle(Height.class, Height::new, new BigDecimal("1.0"));
		assertThat(y, is(sameInstance(x)));
		assertThat(y.get(), is(new BigDecimal("1.0")));
	}

	@Test public void shouldNotRecycleUndiscardedInstance()
	{
		final Height x = RecyclableValue.recycle(Height.class, Height::new, new BigDecimal("12.0"));
		assertThat(x.get(), is(new BigDecimal("12.0")));
		final Height y = RecyclableValue.recycle(Height.class, Height::new, new BigDecimal("1.0"));
		assertThat(y, is(not(sameInstance(x))));
		assertThat(y.get(), is(new BigDecimal("1.0")));
	}

	@Test(expected = AssertionError.class) public void shouldTrapAccessToDiscardedInstance()
	{
		final Height x = new Height(new BigDecimal("12.0"));
		x.discard();
		x.get();
	}
}
