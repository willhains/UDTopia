package org.udtopia.rules;

import java.util.Locale;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class UpperCaseTest
{
	final UpperCase.Rule ruleDefault = new UpperCase.Rule("");
	final UpperCase.Rule ruleEN = new UpperCase.Rule("en");
	final UpperCase.Rule ruleTR = new UpperCase.Rule("tr");

	@Test public void shouldConvertToUpperCaseInDefaultLocale()
	{
		assertThat(ruleDefault.applyTo(getClass(), "AbC"), is("AbC".toUpperCase(Locale.getDefault())));
	}

	@Test public void shouldConvertToUpperCaseInLocaleEN()
	{
		assertThat(ruleEN.applyTo(getClass(), "title"), is("TITLE"));
	}

	@Test public void shouldConvertToUpperCaseInLocaleTR()
	{
		assertThat(ruleTR.applyTo(getClass(), "title"), is("TİTLE"));
	}

	@Test public void shouldIncludeRuleNameAndParamsInToString()
	{
		assertThat(ruleDefault.toString(), is("@UpperCase(locale = " + Locale.getDefault().toLanguageTag() + ")"));
		assertThat(ruleEN.toString(), is("@UpperCase(locale = en)"));
		assertThat(ruleTR.toString(), is("@UpperCase(locale = tr)"));
	}
}
