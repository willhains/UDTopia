package org.udtopia.rules;

import java.lang.annotation.Retention;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.udtopia.assertion.AssertControl;

import static java.lang.annotation.RetentionPolicy.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.runners.MethodSorters.*;
import static org.udtopia.rules.ApplyRuleWhen.*;

@FixMethodOrder(NAME_ASCENDING)
public class RulesCacheTest
{
	@Test public void shouldCacheClassWithNoRules()
	{
		class NoRules { }
		assertThat(DoubleRule.applyRulesFor(NoRules.class, 1.0), is(1.0));
	}

	@Test public void shouldCacheClassWithOneRule()
	{
		@Floor(1) class OneRule { }
		assertThat(DoubleRule.applyRulesFor(OneRule.class, 0.5), is(1.0));
	}

	@Retention(RUNTIME) @interface MalformedRule
	{
		class Rule implements DoubleRule
		{
			@Override public double applyTo(final Class<?> target, final double value) { return value; }
		}
	}

	@Test(expected = RulesError.class) public void shouldTrapNonConformantRuleClass()
	{
		// Rule missing constructor with annotation parameter
		@MalformedRule class A { }
		DoubleRule.RULES.get(A.class);
	}

	@Retention(RUNTIME) @interface BuggyRule
	{
		class Rule implements DoubleRule
		{
			public Rule(final BuggyRule annotation) { throw new NullPointerException(); }

			@Override public double applyTo(final Class<?> target, final double value) { return value; }
		}
	}

	@Test(expected = RulesError.class) public void shouldTrapFailedRuleConstructor()
	{
		// Rule constructor throws exception
		@BuggyRule class A { }
		DoubleRule.RULES.get(A.class);
	}

	@Test(expected = UnsupportedOperationException.class) public void shouldNotSupportRemoveMethod()
	{
		DoubleRule.RULES.remove(Object.class);
	}

	@Test public void shouldIncludeRuleTypeInToString()
	{
		assertThat(DoubleRule.RULES.toString(), containsString("DoubleRule"));
	}

	@Test public void shouldSkipAssertingRulesWhenAssertionsDisabled()
	{
		@Min(value = 5, when = ASSERTS_ENABLED) class A { }
		AssertControl.DISABLE.forClass(A.class);
		final IntRule rule = IntRule.RULES.get(A.class);
		assertThat(rule, is(sameInstance(IntRule.NULL)));
	}

	@Test public void shouldBuildAssertingRuleWhenAssertionsEnabled()
	{
		@Min(value = 5, when = ASSERTS_ENABLED) class A { }
		AssertControl.ENABLE.forClass(A.class);
		final IntRule rule = IntRule.RULES.get(A.class);
		assertThat(rule, is(instanceOf(Min.Rule.class)));
	}

	@Test public void shouldBuildNonAssertingRuleWhenAssertionsDisabled()
	{
		@Min(value = 5, when = ALWAYS) class A { }
		AssertControl.DISABLE.forClass(A.class);
		final IntRule rule = IntRule.RULES.get(A.class);
		assertThat(rule, is(instanceOf(Min.Rule.class)));
	}

	@Test public void shouldBuildNonValidationRuleWhenAssertionsDisabled()
	{
		@Floor(5) class A { }
		AssertControl.DISABLE.forClass(A.class);
		final IntRule rule1 = IntRule.RULES.get(A.class);
		assertThat(rule1, is(instanceOf(Floor.Rule.class)));

		@UpperCase class B { }
		AssertControl.DISABLE.forClass(B.class);
		final StringRule rule2 = StringRule.RULES.get(B.class);
		assertThat(rule2, is(instanceOf(UpperCase.Rule.class)));
	}

	@Test public void shouldExecuteAnnotationsInDeclarationOrder()
	{
		@Min(10) @Max(8) class Impossible1 { }
		final IntRule rule1 = IntRule.RULES.get(Impossible1.class);
		try { rule1.applyTo(Impossible1.class, 9); }
		catch (final ValidationException e) { assertThat(e.getMessage(), containsString("9.0 < 10.0")); }

		@Max(8) @Min(10) class Impossible2 { }
		final IntRule rule2 = IntRule.RULES.get(Impossible2.class);
		try { rule2.applyTo(Impossible2.class, 9); }
		catch (final ValidationException e) { assertThat(e.getMessage(), containsString("9.0 > 8.0")); }
	}

	@Test public void shouldInheritRuleAnnotations()
	{
		@Floor(3) class Parent { }
		@Ceiling(6) class Child extends Parent { }
		assertThat(IntRule.applyRulesFor(Child.class, 7), is(6));
		assertThat(IntRule.applyRulesFor(Child.class, 2), is(3));
	}
}
