package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class StringNormalizerTest
{
	@Test public void shouldInvokeTemplateMethod()
	{
		final StringNormalizer n = value -> value + " world";
		assertThat(n.applyTo(getClass(), "hello"), is("hello world"));
	}
}
