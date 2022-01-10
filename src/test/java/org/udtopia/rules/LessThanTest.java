package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class LessThanTest
{
	@Test public void shouldBuildFromAnnotation()
	{
		@LessThan(1) class Test { }
		final LessThan annotation = Test.class.getAnnotation(LessThan.class);
		assertThat(new LessThan.Rule(annotation), is(not(nullValue())));
	}

	final LessThan.Rule rule = new LessThan.Rule(5);

	@Test(expected = ValidationException.class) public void shouldFailIntValueAboveMax()
	{
		rule.applyTo(getClass(), 6);
	}

	@Test(expected = ValidationException.class) public void shouldFailLongValueAboveMax()
	{
		rule.applyTo(getClass(), 6L);
	}

	@Test(expected = ValidationException.class) public void shouldFailDoubleValueAboveMax()
	{
		rule.applyTo(getClass(), 6.0);
	}

	@Test(expected = ValidationException.class) public void shouldFailIntValueEqualMax()
	{
		rule.applyTo(getClass(), 5);
	}

	@Test(expected = ValidationException.class) public void shouldFailLongValueEqualMax()
	{
		rule.applyTo(getClass(), 5L);
	}

	@Test(expected = ValidationException.class) public void shouldFailDoubleValueEqualMax()
	{
		rule.applyTo(getClass(), 5.0);
	}

	@Test public void shouldPassIntValueBelowMax() { assertThat(rule.applyTo(getClass(), 4), is(4)); }

	@Test public void shouldPassLongValueBelowMax() { assertThat(rule.applyTo(getClass(), 4L), is(4L)); }

	@Test public void shouldPassDoubleValueBelowMax() { assertThat(rule.applyTo(getClass(), 4.0), is(4.0)); }

	final LessThan.Rule negRule = new LessThan.Rule(-2);

	@Test(expected = ValidationException.class) public void shouldFailNegativeInt()
	{
		negRule.applyTo(getClass(), -1);
	}

	@Test(expected = ValidationException.class) public void shouldFailNegativeLong()
	{
		negRule.applyTo(getClass(), -1L);
	}

	@Test(expected = ValidationException.class) public void shouldFailNegativeDouble()
	{
		negRule.applyTo(getClass(), -1.0);
	}

	@Test public void shouldIncludeValueAndLimitInMessage()
	{
		try { rule.applyTo(getClass(), 6); }
		catch (final ValidationException e)
		{
			final String message = e.getMessage();
			assertThat(message, containsString("5"));
			assertThat(message, containsString("6"));
		}
	}

	@Test public void shouldIncludeRuleNameAndParamsInToString()
	{
		assertThat(rule.toString(), is("@LessThan(5.0)"));
	}
}
