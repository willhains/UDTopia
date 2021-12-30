package org.udtopia.example;

import org.junit.Test;
import org.udtopia.example.custom.NotUDTopiaSubclass;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class RuleInCustomClassTest
{
	@Test public void shouldNormalizeAllFields()
	{
		final NotUDTopiaSubclass x = new NotUDTopiaSubclass(-1, -2, -3, "abc");
		assertThat(x.f1, is(0));
		assertThat(x.f2, is(0L));
		assertThat(x.f3, is(0.0));
		assertThat(x.f4, is("ABC"));
	}
}
