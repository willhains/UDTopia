package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class IntRuleTest
{
	@Test public void shouldApplyCombinedRules()
	{
		final IntNormalizer rule1 = value -> value * 3;
		final IntNormalizer rule2 = value -> value + 1;
		final IntRule chain = IntRule.Chain.together(rule1, rule2);
		assertThat(chain.applyTo(getClass(), 25), is(76));
	}

	@Test public void shouldApplyCombinedRulesInOrder()
	{
		final IntValidator rule1 = (target, value) -> { throw new ValidationException(target, "hello"); };
		final IntValidator rule2 = (target, value) -> { throw new ValidationException(target, "world"); };
		final IntRule chain = IntRule.Chain.together(rule1, rule2);
		String errMsg = null;
		try { chain.applyTo(getClass(), 2); }
		catch (final ValidationException e) { errMsg = e.getMessage(); }
		assertThat(errMsg, is("IntRuleTest: hello"));

		final IntNormalizer rule3 = value -> value * 3;
		final IntNormalizer rule4 = value -> value + 1;
		final IntRule chain1 = IntRule.Chain.together(rule3, rule4);
		final IntRule chain2 = IntRule.Chain.together(rule4, rule3);
		assertThat(chain1.applyTo(getClass(), 2), is(7));
		assertThat(chain2.applyTo(getClass(), 2), is(9));

		final IntRule chain4 = IntRule.Chain.together(rule3, rule2);
		try { chain4.applyTo(getClass(), 2); }
		catch (final ValidationException e) { errMsg = e.getMessage(); }
		assertThat(errMsg, is("IntRuleTest: world"));
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test public void shouldPassIfBothValidatorsPass()
	{
		@Min(0) @Max(2) class A { }
		IntRule.applyRulesFor(A.class, 1);
	}

	@Test public void shouldApplyNormalizationsInterleavedWithValidations()
	{
		@Max(2) @Floor(3) @Min(3) @Ceiling(0) class Interleaved { }
		assertThat(IntRule.applyRulesFor(Interleaved.class, 1), is(0));
	}

	@Test public void nullRuleShouldDoNothing()
	{
		final int value = Double.hashCode(Math.random());
		assertThat(IntRule.NULL.applyTo(getClass(), value), is(value));
		IntRule.NULL.applyTo(getClass(), value);
	}

	@Test public void nullRuleShouldDisappearWhenCombining()
	{
		final IntNormalizer rule = value -> value + 1;
		final IntRule chain1 = IntRule.Chain.together(IntRule.NULL, rule);
		final IntRule chain2 = IntRule.Chain.together(rule, IntRule.NULL);
		assertThat(chain1, is(sameInstance(rule)));
		assertThat(chain2, is(sameInstance(rule)));
	}

	@Test public void chainedRuleShouldIncludeAllRulesInToString()
	{
		@Min(2) @Max(20) @Ceiling(19) class A { }
		final IntRule rule = IntRule.RULES.get(A.class);
		assertThat(rule.toString(), is("@Min(2.0) -> @Max(20.0) -> @Ceiling(19.0)"));
	}
}
