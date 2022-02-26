package org.udtopia.rules;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.udtopia.rules.ValidationException.*;

public class ValidationExceptionTest
{
	private String _almostTooLongString;

	@Before public void prepareLongString()
	{
		final char[] chars = new char[VALUE_LENGTH_THRESHOLD - 4];
		Arrays.fill(chars, 'X');
		_almostTooLongString = "abc" + new String(chars); // "abcXXX~XXX" (length = 255)
	}

	@Test public void shouldIncludeClassNameInExceptionMessage()
	{
		class X { }
		final ValidationException x = new ValidationException(X.class, "reason");
		assertThat(x.getMessage(), is("X: reason"));
		final ValidationException y = new ValidationException(X.class, "reason", "value");
		assertThat(y.getMessage(), is("X: reason: value"));
	}

	@Test public void shouldNotAbbreviateStringWhenLessThanThreshold()
	{
		class X { }
		final ValidationException x = new ValidationException(X.class, "reason", _almostTooLongString);
		final String message = x.getMessage();
		assertThat(message, is("X: reason: " + _almostTooLongString));
	}

	@Test public void shouldNotAbbreviateStringWhenEqualToThreshold()
	{
		class X { }
		final ValidationException x = new ValidationException(X.class, "reason", _almostTooLongString + "O");
		final String message = x.getMessage();
		assertThat(message, is("X: reason: " + _almostTooLongString + "O"));
	}

	@Test public void shouldAbbreviateStringWhenBiggerThanThreshold()
	{
		class X { }
		final ValidationException x = new ValidationException(X.class, "reason", _almostTooLongString + "OO");
		final String message = x.getMessage();
		assertThat(message.length(), is("X: reason: ".length() + VALUE_LENGTH_THRESHOLD));
		assertThat(message, startsWith("X: reason: " + "abcXXX"));
		assertThat(message, containsString("..."));
		assertThat(message, endsWith("XXXOO"));
	}
}
