package org.udtopia.recycle;

import org.junit.Test;
import org.udtopia.Value;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class RecyclableStringTest
{
	@RecycleBinSize(1)
	static final @Value class UserId extends RecyclableString<UserId>
	{
		UserId(final String rawValue) { super(UserId::new, rawValue); }
	}

	@Test public void shouldMarkDiscarded()
	{
		final UserId x = new UserId("y");
		assertThat(x.isDiscarded(), is(false));
		x.discard();
		assertThat(x.isDiscarded(), is(true));
	}

	@Test(expected = AssertionError.class) public void shouldTrapDoubleDiscard()
	{
		final UserId x = new UserId("y");
		x.discard();
		x.discard();
	}

	@Test public void shouldRecycleExistingInstance()
	{
		final UserId x = RecyclableString.recycle(UserId.class, UserId::new, "y");
		assertThat(x.get(), is("y"));
		x.discard();
		final UserId y = RecyclableString.recycle(UserId.class, UserId::new, "z");
		assertThat(y, is(sameInstance(x)));
		assertThat(y.get(), is("z"));
	}

	@Test public void shouldNotRecycleUndiscardedInstance()
	{
		final UserId x = RecyclableString.recycle(UserId.class, UserId::new, "y");
		assertThat(x.get(), is("y"));
		final UserId y = RecyclableString.recycle(UserId.class, UserId::new, "z");
		assertThat(y, is(not(sameInstance(x))));
		assertThat(y.get(), is("z"));
	}

	@Test(expected = AssertionError.class) public void shouldTrapAccessToDiscardedInstance()
	{
		final UserId x = new UserId("z");
		x.discard();
		x.get();
	}
}
