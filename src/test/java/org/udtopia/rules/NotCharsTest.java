package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class NotCharsTest
{
	final NotChars.Rule rule = new NotChars.Rule("ABC");

	@Test public void shouldPassCompliantString()
	{
		assertThat(rule.applyTo(getClass(), "DEF"), is("DEF"));
	}

	@Test public void shouldPassEmptyString()
	{
		assertThat(rule.applyTo(getClass(), ""), is(""));
	}

	@Test public void shouldFailStringContainingAtLeastOneDisallowedCharacter()
	{
		String errMsg = null;
		try { rule.applyTo(getClass(), "ACDC"); }
		catch (final ValidationException e) { errMsg = e.getMessage(); }
		assertThat(errMsg, is("NotCharsTest: \"ACDC\" contains invalid characters (invalid = ABC)"));
	}

	@Test public void shouldIncludeDisallowedCharactersInMessage()
	{
		try { rule.validate(getClass(), "aaa"); }
		catch (final ValidationException e)
		{
			final String message = e.getMessage();
			assertThat(message, containsString("ABC"));
			assertThat(message, containsString("aaa"));
		}
	}

	@Test public void shouldIncludeRuleNameAndParamsInToString()
	{
		assertThat(rule.toString(), is("@NotChars(ABC)"));
	}
}
