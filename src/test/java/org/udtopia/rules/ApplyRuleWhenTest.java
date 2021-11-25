package org.udtopia.rules;

import java.lang.annotation.Retention;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.udtopia.assertion.AssertControl;

import static java.lang.annotation.RetentionPolicy.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.udtopia.rules.ApplyRuleWhen.*;

public class ApplyRuleWhenTest
{
	private static final String _FAILED = "failed";

	@Before @After public void resetAssertions() { AssertControl.ENABLE.forClass(getClass()); }

	@Retention(RUNTIME) @interface X
	{
		ApplyRuleWhen when();
	}

	@Test public void shouldApplyAlways()
	{
		@X(when = ALWAYS) class A { }
		assertThat(shouldApplyRule(A.class.getAnnotation(X.class), A.class), is(true));
		AssertControl.DISABLE.forClass(A.class);
		assertThat(shouldApplyRule(A.class.getAnnotation(X.class), A.class), is(true));
	}

	@Test public void shouldApplyOnlyWhenAssertionsEnabled()
	{
		@X(when = ASSERTS_ENABLED) class A { }
		assertThat(shouldApplyRule(A.class.getAnnotation(X.class), A.class), is(true));
		AssertControl.DISABLE.forClass(A.class);
		assertThat(shouldApplyRule(A.class.getAnnotation(X.class), A.class), is(false));
	}

	@Retention(RUNTIME) @interface Y
	{
		int value() default 0;
	}

	@Test public void shouldApplyWhenAnnotationMissingWhenParam()
	{
		@Y class A { }
		assertThat(shouldApplyRule(A.class.getAnnotation(Y.class), A.class), is(true));
	}
}
