package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class GreaterThanTest
{
	final GreaterThan.Rule rule = new GreaterThan.Rule(5);

	@Test(expected = ValidationException.class) public void shouldFailIntValueBelowMin()
	{
		rule.applyTo(getClass(), 4);
	}

	@Test(expected = ValidationException.class) public void shouldFailLongValueBelowMin()
	{
		rule.applyTo(getClass(), 4L);
	}

	@Test(expected = ValidationException.class) public void shouldFailDoubleValueBelowMin()
	{
		rule.applyTo(getClass(), 4.0);
	}

	@Test(expected = ValidationException.class) public void shouldFailIntValueEqualMin()
	{
		rule.applyTo(getClass(), 5);
	}

	@Test(expected = ValidationException.class) public void shouldFailLongValueEqualMin()
	{
		rule.applyTo(getClass(), 5L);
	}

	@Test(expected = ValidationException.class) public void shouldFailDoubleValueEqualMin()
	{
		rule.applyTo(getClass(), 5.0);
	}

	@Test public void shouldPassIntValueAboveMin() { assertThat(rule.applyTo(getClass(), 6), is(6)); }

	@Test public void shouldPassLongValueAboveMin() { assertThat(rule.applyTo(getClass(), 6L), is(6L)); }

	@Test public void shouldPassDoubleValueAboveMin() { assertThat(rule.applyTo(getClass(), 6.0), is(6.0)); }

	final GreaterThan.Rule negRule = new GreaterThan.Rule(-2);

	@Test(expected = ValidationException.class) public void shouldFailNegativeInt()
	{
		assertThat(negRule.applyTo(getClass(), -3), is(-3));
	}

	@Test(expected = ValidationException.class) public void shouldFailNegativeLong()
	{
		assertThat(negRule.applyTo(getClass(), -3L), is(-3L));
	}

	@Test(expected = ValidationException.class) public void shouldFailNegativeDouble()
	{
		assertThat(negRule.applyTo(getClass(), -3.0), is(-3.0));
	}

	@Test public void shouldIncludeValueAndLimitInMessage()
	{
		try { rule.validate(getClass(), 4); }
		catch (final ValidationException e)
		{
			final String message = e.getMessage();
			assertThat(message, containsString("5"));
			assertThat(message, containsString("4"));
		}
	}

	@Test public void shouldIncludeRuleNameAndParamsInToString()
	{
		assertThat(rule.toString(), is("@GreaterThan(5.0)"));
	}
}
