package org.udtopia.assertion;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AssertControlTest
{
	@Before @After public void resetAssertions()
	{
		AssertControl.ENABLE.forClass(getClass());
		AssertControl.DISABLE.forClass(NonAssert.class);
	}

	@Test(expected = AssertionError.class) public void shouldAssertFromJavaAssertStatement()
	{
		assert false : "Java assert statement - expected";
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test public void shouldDisableAssertionStatus()
	{
		AssertControl.DISABLE.forClass(getClass());
		assert false : "Java assert statement - NOT expected";
	}

	private static final class NonAssert
	{
		NonAssert() { assert false : "NonAssert"; }
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test public void shouldSkipAssert()
	{
		new NonAssert();
	}

	@Test(expected = AssertionError.class) public void shouldEnableAssertionStatus()
	{
		AssertControl.ENABLE.forClass(NonAssert.class);
		new NonAssert();
	}
}
