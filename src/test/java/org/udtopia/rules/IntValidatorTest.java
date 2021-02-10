package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class IntValidatorTest
{
	@Test(expected = ValidationException.class) public void shouldThrowFromTemplateMethod()
	{
		final IntValidator v = (target, value) -> { throw new ValidationException(target, "test"); };
		assertThat(v.applyTo(getClass(), 5), is(5));
	}

	@Test public void shouldReturnFromTemplateMethod()
	{
		final IntValidator v = (target, value) -> { };
		assertThat(v.applyTo(getClass(), 5), is(5));
	}
}
