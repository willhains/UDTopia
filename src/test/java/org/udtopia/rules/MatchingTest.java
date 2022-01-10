package org.udtopia.rules;

import java.util.regex.PatternSyntaxException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class MatchingTest
{
	@Test public void shouldBuildFromAnnotation()
	{
		@Matching("abc") class Test { }
		final Matching annotation = Test.class.getAnnotation(Matching.class);
		assertThat(new Matching.Rule(annotation), is(not(nullValue())));
	}

	@Test(expected = PatternSyntaxException.class) public void shouldRejectInvalidRegex()
	{
		new Matching.Rule("(a");
	}

	final Matching.Rule rule = new Matching.Rule("[a-z]-[0-9]");

	@Test(expected = ValidationException.class) public void shouldFailMismatchingString()
	{
		rule.applyTo(getClass(), "A-1");
	}

	@Test(expected = ValidationException.class) public void shouldFailEmptyString()
	{
		rule.applyTo(getClass(), "");
	}

	@Test public void shouldPassMatchingString() { assertThat(rule.applyTo(getClass(), "c-2"), is("c-2")); }

	@Test public void shouldIncludeRegexPatternInMessage()
	{
		try { rule.applyTo(getClass(), "..."); }
		catch (final ValidationException e)
		{
			final String message = e.getMessage();
			assertThat(message, containsString("[a-z]-[0-9]"));
			assertThat(message, containsString("..."));
		}
	}

	@Test public void shouldIncludeRuleNameAndParamsInToString()
	{
		assertThat(rule.toString(), is("@Matching([a-z]-[0-9])"));
	}
}
