package org.udtopia.rules;

import java.util.Locale;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class LowerCaseTest
{
	final LowerCase.Rule ruleDefault = new LowerCase.Rule("");
	final LowerCase.Rule ruleEN = new LowerCase.Rule("en");
	final LowerCase.Rule ruleTR = new LowerCase.Rule("tr");

	@Test public void shouldConvertToLowerCaseInDefaultLocale()
	{
		assertThat(ruleDefault.applyTo(getClass(), "AbC"), is("AbC".toLowerCase(Locale.getDefault())));
	}

	@Test public void shouldConvertToLowerCaseInLocaleEN()
	{
		assertThat(ruleEN.applyTo(getClass(), "TITLE"), is("title"));
	}

	@Test public void shouldConvertToLowerCaseInLocaleTR()
	{
		assertThat(ruleTR.applyTo(getClass(), "TITLE"), is("tıtle"));
	}

	@Test public void shouldIncludeRuleNameAndParamsInToString()
	{
		assertThat(ruleDefault.toString(), is("@LowerCase(locale = " + Locale.getDefault().toLanguageTag() + ")"));
		assertThat(ruleEN.toString(), is("@LowerCase(locale = en)"));
		assertThat(ruleTR.toString(), is("@LowerCase(locale = tr)"));
	}
}
