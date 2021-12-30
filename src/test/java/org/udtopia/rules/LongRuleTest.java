package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class LongRuleTest
{
	@Test public void shouldApplyCombinedRules()
	{
		final LongNormalizer rule1 = value -> value * 3;
		final LongNormalizer rule2 = value -> value + 1;
		final LongRule chain = LongRule.Chain.together(rule1, rule2);
		assertThat(chain.applyTo(getClass(), 25), is(76L));
	}

	@Test public void shouldApplyCombinedRulesInOrder()
	{
		final LongValidator rule1 = (target, value) -> { throw new ValidationException(target, "hello"); };
		final LongValidator rule2 = (target, value) -> { throw new ValidationException(target, "world"); };
		final LongRule chain = LongRule.Chain.together(rule1, rule2);
		String errMsg = null;
		try { chain.applyTo(getClass(), 2); }
		catch (final ValidationException e) { errMsg = e.getMessage(); }
		assertThat(errMsg, is("LongRuleTest: hello"));

		final LongNormalizer rule3 = value -> value * 3;
		final LongNormalizer rule4 = value -> value + 1;
		final LongRule chain1 = LongRule.Chain.together(rule3, rule4);
		final LongRule chain2 = LongRule.Chain.together(rule4, rule3);
		assertThat(chain1.applyTo(getClass(), 2), is(7L));
		assertThat(chain2.applyTo(getClass(), 2), is(9L));

		final LongRule chain4 = LongRule.Chain.together(rule3, rule2);
		try { chain4.applyTo(getClass(), 2); }
		catch (final ValidationException e) { errMsg = e.getMessage(); }
		assertThat(errMsg, is("LongRuleTest: world"));
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test public void shouldPassIfBothValidatorsPass()
	{
		@Min(0) @Max(2) class A { }
		LongRule.applyRulesFor(A.class, 1);
	}

	@Test public void shouldApplyNormalizationsInterleavedWithValidations()
	{
		@Max(2) @Floor(3) @Min(3) @Ceiling(-1) class Interleaved { }
		assertThat(LongRule.applyRulesFor(Interleaved.class, 1L), is(-1L));
	}

	@Test public void nullRuleShouldDoNothing()
	{
		final long value = Double.doubleToLongBits(Math.random());
		assertThat(LongRule.NULL.applyTo(getClass(), value), is(value));
		LongRule.NULL.applyTo(getClass(), value);
	}

	@Test public void nullRuleShouldDisappearWhenCombining()
	{
		final LongNormalizer rule = value -> value + 1;
		final LongRule chain1 = LongRule.Chain.together(LongRule.NULL, rule);
		final LongRule chain2 = LongRule.Chain.together(rule, LongRule.NULL);
		assertThat(chain1, is(sameInstance(rule)));
		assertThat(chain2, is(sameInstance(rule)));
	}

	@Test public void chainedRuleShouldIncludeAllRulesInToString()
	{
		@Min(2) @Max(20) @Ceiling(19) class A { }
		final LongRule rule = LongRule.RULES.get(A.class);
		assertThat(rule.toString(), is("@Min(2.0) -> @Max(20.0) -> @Ceiling(19.0)"));
	}
}
