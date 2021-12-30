package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class CeilingTest
{
	final Ceiling.Rule rule = new Ceiling.Rule(5);

	@Test public void shouldAdjustIntValueAboveCeiling()
	{
		assertThat(rule.applyTo(getClass(), 6), is(5));
	}

	@Test public void shouldAdjustLongValueAboveCeiling()
	{
		assertThat(rule.applyTo(getClass(), 6L), is(5L));
	}

	@Test public void shouldAdjustDoubleValueAboveCeiling()
	{
		assertThat(rule.applyTo(getClass(), 6.0), is(5.0));
	}

	@Test public void shouldNotAdjustIntValueMatchingCeiling()
	{
		assertThat(rule.applyTo(getClass(), 5), is(5));
	}

	@Test public void shouldNotAdjustLongValueMatchingCeiling()
	{
		assertThat(rule.applyTo(getClass(), 5L), is(5L));
	}

	@Test public void shouldNotAdjustDoubleValueMatchingCeiling()
	{
		assertThat(rule.applyTo(getClass(), 5.0), is(5.0));
	}

	@Test public void shouldNotAdjustIntValueBelowCeiling()
	{
		assertThat(rule.applyTo(getClass(), 4), is(4));
	}

	@Test public void shouldNotAdjustLongValueBelowCeiling()
	{
		assertThat(rule.applyTo(getClass(), 4L), is(4L));
	}

	@Test public void shouldNotAdjustDoubleValueBelowCeiling()
	{
		assertThat(rule.applyTo(getClass(), 4.0), is(4.0));
	}

	final Ceiling.Rule negRule = new Ceiling.Rule(-2);

	@Test public void shouldAdjustNegativeIntValueAboveCeiling()
	{
		assertThat(negRule.applyTo(getClass(), -1), is(-2));
	}

	@Test public void shouldAdjustNegativeLongValueAboveCeiling()
	{
		assertThat(negRule.applyTo(getClass(), -1L), is(-2L));
	}

	@Test public void shouldAdjustNegativeDoubleValueAboveCeiling()
	{
		assertThat(negRule.applyTo(getClass(), -1.0), is(-2.0));
	}

	@Test public void shouldIncludeRuleNameAndParamsInToString()
	{
		assertThat(rule.toString(), is("@Ceiling(5.0)"));
	}
}
