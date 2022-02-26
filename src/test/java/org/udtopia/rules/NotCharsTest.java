package org.udtopia.rules;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class NotCharsTest
{
	@Test public void shouldBuildFromAnnotation()
	{
		@NotChars("abc") class Test { }
		final NotChars annotation = Test.class.getAnnotation(NotChars.class);
		assertThat(new NotChars.Rule(annotation), is(not(nullValue())));
	}

	final NotChars.Rule rule = new NotChars.Rule("ABC");

	@Test public void shouldPassCompliantString()
	{
		assertThat(rule.applyTo(getClass(), "DEF"), is("DEF"));
	}

	@Test public void shouldPassEmptyString()
	{
		assertThat(rule.applyTo(getClass(), ""), is(""));
	}

	@Test(expected = ValidationException.class) public void shouldFailStringContainingDisallowedCharacter()
	{
		rule.applyTo(getClass(), "DEAF");
	}

	@Test public void shouldIncludeInvalidCharacterInMessage()
	{
		try
		{
			rule.validate(getClass(), "DEAF");
			Assert.fail("didn't throw exception");
		}
		catch (final ValidationException e)
		{
			final String message = e.getMessage();
			assertThat(message, containsString("'A'"));
		}
	}

	@Test public void shouldIncludeRuleNameAndParamsInToString()
	{
		assertThat(rule.toString(), is("@NotChars(ABC)"));
	}
}
