package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class RoundTest
{
	@Test public void shouldBuildFromAnnotation()
	{
		@Round class Test { }
		final Round annotation = Test.class.getAnnotation(Round.class);
		assertThat(new Round.Rule(annotation), is(not(nullValue())));
	}

	@Test(expected = IllegalArgumentException.class) public void shouldRejectNegativeIncrement()
	{
		new Round.Rule(-1);
	}

	@Test(expected = IllegalArgumentException.class) public void shouldRejectZeroIncrement()
	{
		new Round.Rule(0);
	}

	final Round.Rule ruleDefault = new Round.Rule(Round.DEFAULT_INCREMENT);
	final Round.Rule ruleFraction = new Round.Rule(0.25);
	final Round.Rule ruleMultiple = new Round.Rule(10);

	@Test public void shouldRoundDownToInteger()
	{
		assertThat(ruleDefault.applyTo(getClass(), 3.499), is(3.0));
		assertThat(ruleDefault.applyTo(getClass(), -2.501), is(-3.0));
	}

	@Test public void shouldRoundDownToFraction()
	{
		assertThat(ruleFraction.applyTo(getClass(), 7.374999), is(7.25));
		assertThat(ruleFraction.applyTo(getClass(), -7.12501), is(-7.25));
	}

	@Test public void shouldRoundDownToMultiple()
	{
		assertThat(ruleMultiple.applyTo(getClass(), 34.99), is(30.0));
		assertThat(ruleMultiple.applyTo(getClass(), -25.01), is(-30.0));

		assertThat(ruleMultiple.applyTo(getClass(), 34L), is(30L));
		assertThat(ruleMultiple.applyTo(getClass(), -26L), is(-30L));

		assertThat(ruleMultiple.applyTo(getClass(), 34), is(30));
		assertThat(ruleMultiple.applyTo(getClass(), -26), is(-30));
	}

	@Test public void shouldReturnSameForExactInteger()
	{
		assertThat(ruleDefault.applyTo(getClass(), 3.0), is(3.0));
		assertThat(ruleDefault.applyTo(getClass(), -3.0), is(-3.0));
	}

	@Test public void shouldReturnSameForExactFraction()
	{
		assertThat(ruleFraction.applyTo(getClass(), 11.75), is(11.75));
		assertThat(ruleFraction.applyTo(getClass(), -11.75), is(-11.75));
	}

	@Test public void shouldReturnSameForExactMultiple()
	{
		assertThat(ruleMultiple.applyTo(getClass(), 1140.0), is(1140.0));
		assertThat(ruleMultiple.applyTo(getClass(), -1140.0), is(-1140.0));

		assertThat(ruleMultiple.applyTo(getClass(), 1140L), is(1140L));
		assertThat(ruleMultiple.applyTo(getClass(), -1140L), is(-1140L));

		assertThat(ruleMultiple.applyTo(getClass(), 1140), is(1140));
		assertThat(ruleMultiple.applyTo(getClass(), -1140), is(-1140));
	}

	@Test public void shouldRoundUpToInteger()
	{
		assertThat(ruleDefault.applyTo(getClass(), 3.500), is(4.0));
		assertThat(ruleDefault.applyTo(getClass(), -2.500), is(-2.0));
	}

	@Test public void shouldRoundUpToFraction()
	{
		assertThat(ruleFraction.applyTo(getClass(), 7.375000), is(7.50));
		assertThat(ruleFraction.applyTo(getClass(), -7.12500), is(-7.0));
	}

	@Test public void shouldRoundUpToMultiple()
	{
		assertThat(ruleMultiple.applyTo(getClass(), 35.00), is(40.0));
		assertThat(ruleMultiple.applyTo(getClass(), -25.00), is(-20.0));

		assertThat(ruleMultiple.applyTo(getClass(), 35L), is(40L));
		assertThat(ruleMultiple.applyTo(getClass(), -25L), is(-20L));

		assertThat(ruleMultiple.applyTo(getClass(), 35), is(40));
		assertThat(ruleMultiple.applyTo(getClass(), -25), is(-20));
	}

	@Test public void shouldIncludeRuleNameAndParamsInToString()
	{
		assertThat(ruleDefault.toString(), is("@Round(toNearest = 1.0)"));
	}
}
