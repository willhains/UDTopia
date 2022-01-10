package org.udtopia.rules;

import java.util.stream.Stream;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class ReplaceTest
{
	@Test public void shouldBuildFromAnnotation()
	{
		@Replace(pattern = "abc", with = "ABC") class Test { }
		final Replace annotation = Test.class.getAnnotation(Replace.class);
		assertThat(new Replace.Rule(annotation), is(not(nullValue())));
	}

	final Replace.Rule rule1 = new Replace.Rule("abc", "ABC");
	final Replace.Rule rule2 = new Replace.Rule("[0-9]", "");
	final Replace.Rule rule3 = new Replace.Rule("(\\w+)_(\\w+)", "$2 $2 $1");
	final Replace.Rule rule4 = new Replace.Rule("(\\n+)", "\n");

	@Test public void shouldReplaceNothingWhenNotMatching()
	{
		assertThat(rule1.applyTo(getClass(), "def"), is("def"));
		assertThat(rule2.applyTo(getClass(), "abc"), is("abc"));
		assertThat(rule3.applyTo(getClass(), " _ "), is(" _ "));
		assertThat(rule4.applyTo(getClass(), "abc"), is("abc"));
	}

	@Test public void shouldReplaceAllMatches()
	{
		assertThat(rule1.applyTo(getClass(), "...abc...def...abc"), is("...ABC...def...ABC"));
		assertThat(rule2.applyTo(getClass(), "abc123def456ghi789"), is("abcdefghi"));
		assertThat(rule3.applyTo(getClass(), " a9_d7...1_000"), is(" d7 d7 a9...000 000 1"));
		assertThat(rule4.applyTo(getClass(), "abc\ndef\n\nghi"), is("abc\ndef\nghi"));
	}

	@Test public void shouldPassThroughEmptyStringUntouched()
	{
		Stream.of(rule1, rule2, rule3, rule4).forEach(rule -> assertThat(rule.applyTo(getClass(), ""), is("")));
	}

	@Test public void shouldIncludeRuleNameAndParamsInToString()
	{
		assertThat(rule1.toString(), is("@Replace(pattern = abc, with = ABC)"));
	}
}
