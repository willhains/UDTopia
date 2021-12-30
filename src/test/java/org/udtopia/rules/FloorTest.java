package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class FloorTest
{
	final Floor.Rule rule = new Floor.Rule(5);

	@Test public void shouldAdjustIntValueBelowFloor()
	{
		assertThat(rule.applyTo(getClass(), 4), is(5));
	}

	@Test public void shouldAdjustLongValueBelowFloor()
	{
		assertThat(rule.applyTo(getClass(), 4L), is(5L));
	}

	@Test public void shouldAdjustDoubleValueBelowFloor()
	{
		assertThat(rule.applyTo(getClass(), 4.0), is(5.0));
	}

	@Test public void shouldNotAdjustIntValueMatchingFloor()
	{
		assertThat(rule.applyTo(getClass(), 5), is(5));
	}

	@Test public void shouldNotAdjustLongValueMatchingFloor()
	{
		assertThat(rule.applyTo(getClass(), 5L), is(5L));
	}

	@Test public void shouldNotAdjustDoubleValueMatchingFloor()
	{
		assertThat(rule.applyTo(getClass(), 5.0), is(5.0));
	}

	@Test public void shouldNotAdjustIntValueAboveFloor()
	{
		assertThat(rule.applyTo(getClass(), 6), is(6));
	}

	@Test public void shouldNotAdjustLongValueAboveFloor()
	{
		assertThat(rule.applyTo(getClass(), 6L), is(6L));
	}

	@Test public void shouldNotAdjustDoubleValueAboveFloor()
	{
		assertThat(rule.applyTo(getClass(), 6.0), is(6.0));
	}

	final Floor.Rule negRule = new Floor.Rule(-2);

	@Test public void shouldAdjustNegativeIntValueBelowFloor()
	{
		assertThat(negRule.applyTo(getClass(), -3), is(-2));
	}

	@Test public void shouldAdjustNegativeLongValueBelowFloor()
	{
		assertThat(negRule.applyTo(getClass(), -3L), is(-2L));
	}

	@Test public void shouldAdjustNegativeDoubleValueBelowFloor()
	{
		assertThat(negRule.applyTo(getClass(), -3.0), is(-2.0));
	}

	@Test public void shouldIncludeRuleNameAndParamsInToString()
	{
		assertThat(rule.toString(), is("@Floor(5.0)"));
	}
}
