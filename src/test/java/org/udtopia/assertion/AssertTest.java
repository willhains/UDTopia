package org.udtopia.assertion;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

public class AssertTest
{
	@Before @After public void resetAssertions() { AssertControl.ENABLE.forClass(Assert.class); }

	@Test public void shouldBeEnabled() { assertThat(Assert.isEnabled(), is(true)); }

	@Test public void shouldBeDisabled()
	{
		AssertControl.DISABLE.forClass(Assert.class);
		assertThat(Assert.isEnabled(), is(false));
	}

	@Test(expected = AssertionError.class) public void shouldAssertTrue() { Assert.that(() -> false); }

	@Test public void shouldAssertTrueWithMessage()
	{
		try { Assert.that(() -> false, "123"); }
		catch (final AssertionError e) { assertThat(e.getMessage(), is("123")); }
	}

	@Test(expected = AssertionError.class) public void shouldAssertFalse() { Assert.not(() -> true); }

	@Test public void shouldAssertFalseWithMessage()
	{
		try { Assert.not(() -> true, "123"); }
		catch (final AssertionError e) { assertThat(e.getMessage(), is("123")); }
	}

	@Test(expected = AssertionError.class) public void shouldAssertNotNull() { Assert.notNull(() -> null); }

	@Test public void shouldAssertNotNullWithMessage()
	{
		try { Assert.notNull(() -> null, "123"); }
		catch (final AssertionError e) { assertThat(e.getMessage(), is("123")); }
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test public void shouldNotAssertTrue() { Assert.that(() -> true); }

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test public void shouldNotAssertTrueWithMessage() { Assert.that(() -> true, "123"); }

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test public void shouldNotAssertFalse() { Assert.not(() -> false); }

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test public void shouldNotAssertFalseWithMessage() { Assert.not(() -> false, "123"); }

	@Test public void shouldPerformDebugTask()
	{
		final Runnable task = mock(Runnable.class);
		Assert.debug(task);
		verify(task, times(1)).run();
	}
}
