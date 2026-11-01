package org.udtopia.assertion;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AssertControlTest
{
	@Before @After public void resetAssertions()
	{
		AssertControl.ENABLE.forClass(getClass());
		AssertControl.DISABLE.forClass(_NonAssert.class);
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

	private static final class _NonAssert
	{
		_NonAssert() { assert false : "NonAssert"; }
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test public void shouldSkipAssert()
	{
		new _NonAssert();
	}

	@Test(expected = AssertionError.class) public void shouldEnableAssertionStatus()
	{
		AssertControl.ENABLE.forClass(_NonAssert.class);
		new _NonAssert();
	}
}
