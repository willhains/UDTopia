package org.udtopia.rules;

import java.util.Locale;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class StringRuleTest
{
	@Test public void shouldApplyCombinedRules()
	{
		final StringNormalizer rule1 = value -> value.toUpperCase(Locale.ENGLISH);
		final StringNormalizer rule2 = value -> value + "...";
		final StringRule chain = StringRule.Chain.together(rule1, rule2);
		assertThat(chain.applyTo(getClass(), "abc"), is("ABC..."));
	}

	@Test public void shouldApplyCombinedRulesInOrder()
	{
		final StringValidator rule1 = (target, value) -> { throw new ValidationException(target, "hello"); };
		final StringValidator rule2 = (target, value) -> { throw new ValidationException(target, "world"); };
		final StringRule chain1 = StringRule.Chain.together(rule1, rule2);
		String errMsg = null;
		try { chain1.applyTo(getClass(), "abc"); }
		catch (final ValidationException e) { errMsg = e.getMessage(); }
		assertThat(errMsg, is("StringRuleTest: hello"));

		final StringNormalizer rule3 = value -> value + ">";
		final StringNormalizer rule4 = value -> value + "<";
		final StringRule chain2 = StringRule.Chain.together(rule3, rule4);
		final StringRule chain3 = StringRule.Chain.together(rule4, rule3);
		assertThat(chain2.applyTo(getClass(), "abc"), is("abc><"));
		assertThat(chain3.applyTo(getClass(), "abc"), is("abc<>"));

		final StringRule chain4 = StringRule.Chain.together(rule3, rule2);
		try { chain4.applyTo(getClass(), "abc"); }
		catch (final ValidationException e) { errMsg = e.getMessage(); }
		assertThat(errMsg, is("StringRuleTest: world"));
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test public void shouldPassIfBothValidatorsPass()
	{
		@Min(0) @Max(2) class A { }
		StringRule.applyRulesFor(A.class, "x");
	}

	@Test public void shouldApplyNormalizationsInterleavedWithValidations()
	{
		@Trim @NotChars(" ABC") @Replace(pattern = "_", with = " ") @UpperCase class Interleaved { }
		assertThat(StringRule.applyRulesFor(Interleaved.class, " a_b_c "), is("A B C"));
	}

	@Test public void nullRuleShouldDoNothing()
	{
		final String value = Double.toHexString(Math.random());
		assertThat(StringRule.NULL.applyTo(getClass(), value), is(value));
		StringRule.NULL.applyTo(getClass(), value);
	}

	@Test public void nullRuleShouldDisappearWhenCombining()
	{
		final StringNormalizer rule = value -> value.toUpperCase(Locale.ENGLISH);
		final StringRule chain1 = StringRule.Chain.together(StringRule.NULL, rule);
		final StringRule chain2 = StringRule.Chain.together(rule, StringRule.NULL);
		assertThat(chain1, is(sameInstance(rule)));
		assertThat(chain2, is(sameInstance(rule)));
	}

	@Test public void chainedRuleShouldIncludeAllRulesInToString()
	{
		@Min(2) @Max(20) @Trim class A { }
		final StringRule rule = StringRule.RULES.get(A.class);
		assertThat(rule.toString(), is("@Min(2.0) -> @Max(20.0) -> @Trim"));
	}
}
