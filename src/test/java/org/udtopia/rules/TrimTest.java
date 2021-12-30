package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class TrimTest
{
	final Trim.Rule rule = new Trim.Rule();

	@SuppressWarnings("HardcodedLineSeparator")
	@Test public void shouldTrimWhitespace()
	{
		assertThat(rule.applyTo(getClass(), " \t\r\nabc\r\n\t "), is("abc"));
	}

	@Test public void shouldIncludeRuleNameAndParamsInToString()
	{
		assertThat(rule.toString(), is("@Trim"));
	}
}
