package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class IntNormalizerTest
{
	@Test public void shouldInvokeTemplateMethod()
	{
		final IntNormalizer n = value -> value + 1;
		assertThat(n.applyTo(getClass(), 5), is(6));
	}
}
