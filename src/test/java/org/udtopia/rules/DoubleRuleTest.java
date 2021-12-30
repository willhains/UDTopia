package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class DoubleRuleTest
{
	@Test public void shouldApplyCombinedRules()
	{
		final DoubleNormalizer rule1 = value -> value * 3;
		final DoubleNormalizer rule2 = value -> value + 1;
		final DoubleRule chain = DoubleRule.Chain.together(rule1, rule2);
		assertThat(chain.applyTo(getClass(), 25), is(76.0));
	}

	@Test public void shouldApplyCombinedRulesInOrder()
	{
		final DoubleValidator rule1 = (target, value) -> { throw new ValidationException(target, "hello"); };
		final DoubleValidator rule2 = (target, value) -> { throw new ValidationException(target, "world"); };
		final DoubleRule chain = DoubleRule.Chain.together(rule1, rule2);
		String errMsg = null;
		try { chain.applyTo(getClass(), 2); }
		catch (final ValidationException e) { errMsg = e.getMessage(); }
		assertThat(errMsg, is("DoubleRuleTest: hello"));

		final DoubleNormalizer rule3 = value -> value * 3;
		final DoubleNormalizer rule4 = value -> value + 1;
		final DoubleRule chain1 = DoubleRule.Chain.together(rule3, rule4);
		final DoubleRule chain2 = DoubleRule.Chain.together(rule4, rule3);
		assertThat(chain1.applyTo(getClass(), 2), is(7.0));
		assertThat(chain2.applyTo(getClass(), 2), is(9.0));

		final DoubleRule chain4 = DoubleRule.Chain.together(rule3, rule2);
		try { chain4.applyTo(getClass(), 2); }
		catch (final ValidationException e) { errMsg = e.getMessage(); }
		assertThat(errMsg, is("DoubleRuleTest: world"));
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test public void shouldPassIfBothValidatorsPass()
	{
		@Min(0) @Max(2) class A { }
		DoubleRule.applyRulesFor(A.class, 1);
	}

	@Test public void shouldApplyNormalizationsInterleavedWithValidations()
	{
		@Max(2) @Floor(3) @Min(3) @Ceiling(0) class Interleaved { }
		assertThat(DoubleRule.applyRulesFor(Interleaved.class, 1.0), is(0.0));
	}

	@Test public void nullRuleShouldDoNothing()
	{
		final double value = Math.random();
		assertThat(DoubleRule.NULL.applyTo(getClass(), value), is(value));
		DoubleRule.NULL.applyTo(getClass(), value);
	}

	@Test public void nullRuleShouldDisappearWhenCombining()
	{
		final DoubleNormalizer rule = value -> value + 1;
		final DoubleRule chain1 = DoubleRule.Chain.together(DoubleRule.NULL, rule);
		final DoubleRule chain2 = DoubleRule.Chain.together(rule, DoubleRule.NULL);
		assertThat(chain1, is(sameInstance(rule)));
		assertThat(chain2, is(sameInstance(rule)));
	}

	@Test public void chainedRuleShouldIncludeAllRulesInToString()
	{
		@Min(2) @Max(20) @Ceiling(19) class A { }
		final DoubleRule rule = DoubleRule.RULES.get(A.class);
		assertThat(rule.toString(), is("@Min(2.0) -> @Max(20.0) -> @Ceiling(19.0)"));
	}
}
