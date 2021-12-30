package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class MinTest
{
	final Min.Rule assertRule = new Min.Rule(5);
	final Min.Rule rule = new Min.Rule(5);

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

	@Test(expected = ValidationException.class) public void shouldFailLongString()
	{
		rule.applyTo(getClass(), "1234");
	}

	@Test public void shouldPassIntValueEqualMin()
	{
		assertThat(assertRule.applyTo(getClass(), 5), is(5));
		assertThat(rule.applyTo(getClass(), 5), is(5));
	}

	@Test public void shouldPassLongValueEqualMin()
	{
		assertThat(assertRule.applyTo(getClass(), 5L), is(5L));
		assertThat(rule.applyTo(getClass(), 5L), is(5L));
	}

	@Test public void shouldPassDoubleValueEqualMin()
	{
		assertThat(assertRule.applyTo(getClass(), 5.0), is(5.0));
		assertThat(rule.applyTo(getClass(), 5.0), is(5.0));
	}

	@Test public void shouldPassMatchingLengthString()
	{
		assertThat(assertRule.applyTo(getClass(), "12345"), is("12345"));
		assertThat(rule.applyTo(getClass(), "12345"), is("12345"));
	}

	@Test public void shouldPassIntValueAboveMin()
	{
		assertThat(assertRule.applyTo(getClass(), 6), is(6));
		assertThat(rule.applyTo(getClass(), 6), is(6));
	}

	@Test public void shouldPassLongValueAboveMin()
	{
		assertThat(assertRule.applyTo(getClass(), 6L), is(6L));
		assertThat(rule.applyTo(getClass(), 6L), is(6L));
	}

	@Test public void shouldPassDoubleValueAboveMin()
	{
		assertThat(assertRule.applyTo(getClass(), 6.0), is(6.0));
		assertThat(rule.applyTo(getClass(), 6.0), is(6.0));
	}

	@Test public void shouldPassShortString()
	{
		assertThat(assertRule.applyTo(getClass(), "123456"), is("123456"));
		assertThat(rule.applyTo(getClass(), "123456"), is("123456"));
	}

	final Min.Rule negRule = new Min.Rule(-2);

	@Test(expected = ValidationException.class) public void shouldFailNegativeInt()
	{
		negRule.applyTo(getClass(), -3);
	}

	@Test(expected = ValidationException.class) public void shouldFailNegativeLong()
	{
		negRule.applyTo(getClass(), -3L);
	}

	@Test(expected = ValidationException.class) public void shouldFailNegativeDouble()
	{
		negRule.applyTo(getClass(), -3.0);
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
		assertThat(assertRule.toString(), is("@Min(5.0)"));
		assertThat(rule.toString(), is("@Min(5.0)"));
	}
}
