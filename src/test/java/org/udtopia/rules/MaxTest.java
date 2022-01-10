package org.udtopia.rules;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class MaxTest
{
	@Test public void shouldBuildFromAnnotation()
	{
		@Max(1) class Test { }
		final Max annotation = Test.class.getAnnotation(Max.class);
		assertThat(new Max.Rule(annotation), is(not(nullValue())));
	}

	final Max.Rule assertRule = new Max.Rule(5);
	final Max.Rule rule = new Max.Rule(5);

	@Test(expected = ValidationException.class) public void shouldFailIntValueAboveMax()
	{
		assertThat(rule.applyTo(getClass(), 6), is(6));
	}

	@Test(expected = ValidationException.class) public void shouldFailLongValueAboveMax()
	{
		assertThat(rule.applyTo(getClass(), 6L), is(6L));
	}

	@Test(expected = ValidationException.class) public void shouldFailDoubleValueAboveMax()
	{
		assertThat(rule.applyTo(getClass(), 6.0), is(6.0));
	}

	@Test(expected = ValidationException.class) public void shouldFailLongString()
	{
		assertThat(rule.applyTo(getClass(), "123456"), is("123456"));
	}

	@Test public void shouldAbbreviateStringWhenMuchBiggerThanLimit()
	{
		try
		{
			new Max.Rule(1).applyTo(getClass(), "1234567890abcdefgh");
			Assert.fail("Didn't throw exception");
		}
		catch (final ValidationException e)
		{
			final String valuePartOfMessage = e.getMessage().split("\"")[1];
			assertThat(valuePartOfMessage.length(), is(1 + Max.Rule.STRING_LENGTH_THRESHOLD));
			assertThat(valuePartOfMessage, startsWith("123"));
			assertThat(valuePartOfMessage, containsString("..."));
			assertThat(valuePartOfMessage, endsWith("fgh"));
		}

		try
		{
			new Max.Rule(2).applyTo(getClass(), "1234567890abcdefghi");
			Assert.fail("Didn't throw exception");
		}
		catch (final ValidationException e)
		{
			final String valuePartOfMessage = e.getMessage().split("\"")[1];
			assertThat(valuePartOfMessage.length(), is(2 + Max.Rule.STRING_LENGTH_THRESHOLD));
			assertThat(valuePartOfMessage, startsWith("123"));
			assertThat(valuePartOfMessage, containsString("..."));
			assertThat(valuePartOfMessage, endsWith("ghi"));
		}

		try
		{
			new Max.Rule(3).applyTo(getClass(), "1234567890abcdefghij");
			Assert.fail("Didn't throw exception");
		}
		catch (final ValidationException e)
		{
			final String valuePartOfMessage = e.getMessage().split("\"")[1];
			assertThat(valuePartOfMessage.length(), is(3 + Max.Rule.STRING_LENGTH_THRESHOLD));
			assertThat(valuePartOfMessage, startsWith("123"));
			assertThat(valuePartOfMessage, containsString("..."));
			assertThat(valuePartOfMessage, endsWith("hij"));
		}

		try
		{
			new Max.Rule(4).applyTo(getClass(), "1234567890abcdefghijk");
			Assert.fail("Didn't throw exception");
		}
		catch (final ValidationException e)
		{
			final String valuePartOfMessage = e.getMessage().split("\"")[1];
			assertThat(valuePartOfMessage.length(), is(4 + Max.Rule.STRING_LENGTH_THRESHOLD));
			assertThat(valuePartOfMessage, startsWith("123"));
			assertThat(valuePartOfMessage, containsString("..."));
			assertThat(valuePartOfMessage, endsWith("ijk"));
		}
	}

	@Test public void shouldNotAbbreviateStringWhenNotMuchBiggerThanLimit()
	{
		try
		{
			new Max.Rule(1).applyTo(getClass(), "1234567890abcdefg");
			Assert.fail("Didn't throw exception");
		}
		catch (final ValidationException e)
		{
			final String valuePartOfMessage = e.getMessage().split("\"")[1];
			assertThat(valuePartOfMessage.length(), is(1 + Max.Rule.STRING_LENGTH_THRESHOLD));
			assertThat(valuePartOfMessage, is("1234567890abcdefg"));
		}

		try
		{
			new Max.Rule(2).applyTo(getClass(), "1234567890abcdefgh");
			Assert.fail("Didn't throw exception");
		}
		catch (final ValidationException e)
		{
			final String valuePartOfMessage = e.getMessage().split("\"")[1];
			assertThat(valuePartOfMessage.length(), is(2 + Max.Rule.STRING_LENGTH_THRESHOLD));
			assertThat(valuePartOfMessage, is("1234567890abcdefgh"));
		}
	}

	@Test public void shouldPassIntValueEqualMax()
	{
		assertThat(assertRule.applyTo(getClass(), 5), is(5));
		assertThat(rule.applyTo(getClass(), 5), is(5));
	}

	@Test public void shouldPassLongValueEqualMax()
	{
		assertThat(assertRule.applyTo(getClass(), 5L), is(5L));
		assertThat(rule.applyTo(getClass(), 5L), is(5L));
	}

	@Test public void shouldPassDoubleValueEqualMax()
	{
		assertThat(assertRule.applyTo(getClass(), 5.0), is(5.0));
		assertThat(rule.applyTo(getClass(), 5.0), is(5.0));
	}

	@Test public void shouldPassMatchingLengthString()
	{
		assertThat(assertRule.applyTo(getClass(), "12345"), is("12345"));
		assertThat(rule.applyTo(getClass(), "12345"), is("12345"));
	}

	@Test public void shouldPassIntValueBelowMax()
	{
		assertThat(assertRule.applyTo(getClass(), 4), is(4));
		assertThat(rule.applyTo(getClass(), 4), is(4));
	}

	@Test public void shouldPassLongValueBelowMax()
	{
		assertThat(assertRule.applyTo(getClass(), 4L), is(4L));
		assertThat(rule.applyTo(getClass(), 4L), is(4L));
	}

	@Test public void shouldPassDoubleValueBelowMax()
	{
		assertThat(assertRule.applyTo(getClass(), 4.0), is(4.0));
		assertThat(rule.applyTo(getClass(), 4.0), is(4.0));
	}

	@Test public void shouldPassShortString()
	{
		assertThat(assertRule.applyTo(getClass(), "1234"), is("1234"));
		assertThat(rule.applyTo(getClass(), "1234"), is("1234"));
	}

	final Max.Rule negRule = new Max.Rule(-5);

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
		try { rule.validate(getClass(), 6); }
		catch (final ValidationException e)
		{
			final String message = e.getMessage();
			assertThat(message, containsString("5"));
			assertThat(message, containsString("6"));
		}
	}

	@Test public void shouldIncludeRuleNameAndParamsInToString()
	{
		assertThat(rule.toString(), is("@Max(5.0)"));
	}
}
