package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class StringValidatorTest
{
	@Test(expected = ValidationException.class) public void shouldThrowFromTemplateMethod()
	{
		final StringValidator v = (target, value) -> { throw new ValidationException(target, "test"); };
		v.applyTo(getClass(), "abc");
	}

	@Test public void shouldReturnFromTemplateMethod()
	{
		final StringValidator v = (target, value) -> { };
		assertThat(v.applyTo(getClass(), "abc"), is("abc"));
	}
}
