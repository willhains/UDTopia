package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class MultipleOfTest
{
	@Test public void shouldBuildFromAnnotation()
	{
		@MultipleOf(1) class Test { }
		final MultipleOf annotation = Test.class.getAnnotation(MultipleOf.class);
		assertThat(new MultipleOf.Rule(annotation), is(not(nullValue())));
	}

	@Test(expected = IllegalArgumentException.class) public void shouldRejectNegativeIncrement()
	{
		new MultipleOf.Rule(-1);
	}

	@Test(expected = IllegalArgumentException.class) public void shouldRejectZeroIncrement()
	{
		new MultipleOf.Rule(0);
	}

	final MultipleOf.Rule rule1 = new MultipleOf.Rule(5);

	@Test(expected = ValidationException.class) public void shouldFailIntValueLessThanWholeIncrement()
	{
		assertThat(rule1.applyTo(getClass(), 3), is(3));
	}

	@Test(expected = ValidationException.class) public void shouldFailIntValueGreaterThanWholeIncrement()
	{
		assertThat(rule1.applyTo(getClass(), 9), is(9));
	}

	@Test(expected = ValidationException.class) public void shouldFailLongValueLessThanWholeIncrement()
	{
		assertThat(rule1.applyTo(getClass(), 3L), is(3L));
	}

	@Test(expected = ValidationException.class) public void shouldFailLongValueGreaterThanWholeIncrement()
	{
		assertThat(rule1.applyTo(getClass(), 9L), is(9L));
	}

	@Test public void shouldPassIntValueMatchingWholeIncrement()
	{
		assertThat(rule1.applyTo(getClass(), 5), is(5));
	}

	@Test public void shouldPassLongValueMatchingWholeIncrement()
	{
		assertThat(rule1.applyTo(getClass(), 5L), is(5L));
	}

	@Test public void shouldPassIntValueMultipleOfWholeIncrement()
	{
		assertThat(rule1.applyTo(getClass(), 15), is(15));
	}

	@Test public void shouldPassLongValueMultipleOfWholeIncrement()
	{
		assertThat(rule1.applyTo(getClass(), 15L), is(15L));
	}

	@Test public void shouldIncludeRegexPatternInMessage()
	{
		try { rule1.applyTo(getClass(), 6); }
		catch (final ValidationException e)
		{
			final String message = e.getMessage();
			assertThat(message, containsString("5"));
			assertThat(message, containsString("6"));
		}
	}

	@Test public void shouldIncludeRuleNameAndParamsInToString()
	{
		assertThat(rule1.toString(), is("@MultipleOf(5)"));
	}
}
