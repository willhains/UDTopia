package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class EncodableAsTest
{

	@Test public void shouldBuildFromAnnotation()
	{
		@EncodableAs("US-ASCII") class Test { }
		final EncodableAs annotation = Test.class.getAnnotation(EncodableAs.class);
		assertThat(new EncodableAs.Rule(annotation), is(not(nullValue())));
	}

	@Test(expected = IllegalArgumentException.class) public void shouldRejectInvalidEncodingName()
	{
		new EncodableAs.Rule("blah");
	}

	final EncodableAs.Rule ascii = new EncodableAs.Rule("US-ASCII");
	final EncodableAs.Rule eucjp = new EncodableAs.Rule("EUC-JP");
	final EncodableAs.Rule utf8 = new EncodableAs.Rule("UTF-8");

	@Test public void shouldAcceptAsciiText()
	{
		ascii.validate(getClass(), "abc");
	}

	@Test(expected = ValidationException.class) public void shouldRejectNonAsciiText()
	{
		ascii.validate(getClass(), "Montréal");
	}

	@Test public void shouldAcceptEncodableText()
	{
		eucjp.validate(getClass(), "東京オリンピック２０２０");
	}

	@Test(expected = ValidationException.class) public void shouldRejectNonEncodableText()
	{
		eucjp.validate(getClass(), "2018 평창 올림픽");
	}

	@Test public void shouldAcceptEverything()
	{
		utf8.validate(getClass(), "abc");
		utf8.validate(getClass(), "Montréal");
		utf8.validate(getClass(), "東京オリンピック２０２０");
		utf8.validate(getClass(), "2018 평창 올림픽");
		utf8.validate(getClass(), "सगरमाथा");
	}

	@Test public void shouldIncludeEncodingNameInMessage()
	{
		class X { }
		try { ascii.applyTo(X.class, "Montréal"); }
		catch (final ValidationException e)
		{
			final String message = e.getMessage();
			assertThat(message, containsString("US-ASCII"));
			assertThat(message, containsString("Montréal"));
		}
	}

	@Test public void shouldIncludeRuleNameAndParamsInToString()
	{
		assertThat(ascii.toString(), is("@EncodableAs(US-ASCII)"));
	}
}
