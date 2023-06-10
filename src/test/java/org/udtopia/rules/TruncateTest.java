package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.udtopia.rules.Truncate.TruncateFrom.*;

public class TruncateTest
{
	@Test public void shouldBuildFromAnnotation()
	{
		@Truncate(toLength = 10, from = MIDDLE, ellipsis = "...") class Test { }
		final Truncate annotation = Test.class.getAnnotation(Truncate.class);
		assertThat(new Truncate.Rule(annotation), is(not(nullValue())));
	}

	@Test(expected = IllegalArgumentException.class) public void shouldRejectNegativeLength()
	{
		new Truncate.Rule(-1, RIGHT, "");
	}

	@Test(expected = IllegalArgumentException.class) public void shouldRejectZeroLength()
	{
		new Truncate.Rule(0, RIGHT, "");
	}

	@Test(expected = IllegalArgumentException.class) public void shouldRejectTooLongEllipsis()
	{
		new Truncate.Rule(2, RIGHT, "...");
	}

	@Test public void shouldNotTruncate()
	{
		final Truncate.Rule rule = new Truncate.Rule(10, RIGHT, "");
		assertThat(rule.applyTo(getClass(), "0123456789"), is("0123456789"));
	}

	@Test public void shouldTruncateFromRight()
	{
		final Truncate.Rule rule = new Truncate.Rule(10, RIGHT, "");
		assertThat(rule.applyTo(getClass(), "0123456789a"), is("0123456789"));
	}

	@Test public void shouldTruncateFromLeft()
	{
		final Truncate.Rule rule = new Truncate.Rule(10, LEFT, "");
		assertThat(rule.applyTo(getClass(), "0123456789a"), is("123456789a"));
	}

	@Test public void shouldTruncateFromMiddle()
	{
		final Truncate.Rule rule = new Truncate.Rule(10, MIDDLE, "");
		assertThat(rule.applyTo(getClass(), "0123456789a"), is("012346789a"));
	}

	@Test public void shouldReturnEllipsisItself()
	{
		final Truncate.Rule rule = new Truncate.Rule(3, RIGHT, "...");
		assertThat(rule.applyTo(getClass(), "123"), is("123"));
		assertThat(rule.applyTo(getClass(), "0123"), is("..."));
	}

	@Test public void shouldTruncateFromRightWithEllipsis()
	{
		final Truncate.Rule rule = new Truncate.Rule(10, RIGHT, "...");
		assertThat(rule.applyTo(getClass(), "0123456789a"), is("0123456..."));
	}

	@Test public void shouldTruncateFromLeftWithEllipsis()
	{
		final Truncate.Rule rule = new Truncate.Rule(10, LEFT, "...");
		assertThat(rule.applyTo(getClass(), "0123456789a"), is("...456789a"));
	}

	@Test public void shouldTruncateFromMiddleWithEllipsis()
	{
		final Truncate.Rule rule = new Truncate.Rule(10, MIDDLE, "...");
		assertThat(rule.applyTo(getClass(), "0123456789a"), is("012...789a"));
	}
}
