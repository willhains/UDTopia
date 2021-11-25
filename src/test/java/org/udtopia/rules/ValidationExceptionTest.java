package org.udtopia.rules;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class ValidationExceptionTest
{
	@Test public void shouldIncludeClassNameInExceptionMessage()
	{
		class X { }
		final ValidationException x = new ValidationException(X.class, "test");
		assertThat(x.getMessage(), is("X: test"));
	}
}
