package org.udtopia.rules;

import java.util.regex.PatternSyntaxException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class NotMatchingTest
{
	@Test public void shouldBuildFromAnnotation()
	{
		@NotMatching("abc") class Test { }
		final NotMatching annotation = Test.class.getAnnotation(NotMatching.class);
		assertThat(new NotMatching.Rule(annotation), is(not(nullValue())));
	}

	@Test(expected = PatternSyntaxException.class) public void shouldRejectInvalidRegex()
	{
		new NotMatching.Rule("(a");
	}

	final NotMatching.Rule rule = new NotMatching.Rule("[a-z]-[0-9]");

	@Test public void shouldPassMismatchingString() { assertThat(rule.applyTo(getClass(), "A-1"), is("A-1")); }

	@Test public void shouldPassEmptyString() { assertThat(rule.applyTo(getClass(), ""), is("")); }

	@Test(expected = ValidationException.class) public void shouldFailMatchingString()
	{
		assertThat(rule.applyTo(getClass(), "c-2"), is("c-2"));
	}

	@Test public void shouldIncludeRegexPatternInMessage()
	{
		try { rule.applyTo(getClass(), "abc-123"); }
		catch (final ValidationException e)
		{
			final String message = e.getMessage();
			assertThat(message, containsString("[a-z]-[0-9]"));
			assertThat(message, containsString("abc-123"));
		}
	}

	@Test public void shouldIncludeRuleNameAndParamsInToString()
	{
		assertThat(rule.toString(), is("@NotMatching([a-z]-[0-9])"));
	}
}
