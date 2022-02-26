package org.udtopia.rules;

import org.junit.Assert;
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

	@Test(expected = ValidationException.class) public void shouldFailStringContainingDisallowedCharacter()
	{
		rule.applyTo(getClass(), "CABx");
	}

	@Test public void shouldIncludeInvalidCharacterInMessage()
	{
		try
		{
			rule.validate(getClass(), "CABx");
			Assert.fail("didn't throw exception");
		}
		catch (final ValidationException e)
		{
			final String message = e.getMessage();
			assertThat(message, containsString("'x'"));
		}
	}

	@Test public void shouldIncludeRuleNameAndParamsInToString()
	{
		assertThat(rule.toString(), is("@Chars(ABC)"));
	}
}
