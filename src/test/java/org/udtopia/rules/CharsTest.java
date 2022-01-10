package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class CharsTest
{
	@Test public void shouldBuildFromAnnotation()
	{
		@Chars("abc") class Test { }
		final Chars annotation = Test.class.getAnnotation(Chars.class);
		assertThat(new Chars.Rule(annotation), is(not(nullValue())));
	}

	final Chars.Rule rule = new Chars.Rule("ABC");

	@Test public void shouldPassCompliantString()
	{
		assertThat(rule.applyTo(getClass(), "CAB"), is("CAB"));
	}

	@Test public void shouldPassEmptyString()
	{
		assertThat(rule.applyTo(getClass(), ""), is(""));
	}

	@Test public void shouldFailStringContainingAtLeastOneDisallowedCharacter()
	{
		String errMsg = null;
		try { rule.applyTo(getClass(), "acdc"); }
		catch (final ValidationException e) { errMsg = e.getMessage(); }
		assertThat(errMsg, is("CharsTest: \"acdc\" contains invalid characters (valid = ABC)"));
	}

	@Test public void shouldIncludeAllowedCharactersInMessage()
	{
		try { rule.validate(getClass(), "bbb"); }
		catch (final ValidationException e)
		{
			final String message = e.getMessage();
			assertThat(message, containsString("ABC"));
			assertThat(message, containsString("bbb"));
		}
	}

	@Test public void shouldIncludeRuleNameAndParamsInToString()
	{
		assertThat(rule.toString(), is("@Chars(ABC)"));
	}
}
