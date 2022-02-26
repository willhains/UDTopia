package org.udtopia.rules;

/**
 * Invalid raw value.
 */
public class ValidationException extends RuntimeException
{
	private static final long serialVersionUID = -8889969252399252944L;

	/**
	 * @param target the class declaring the violated rule.
	 * @param failureReason a helpful description of the error.
	 */
	public ValidationException(final Class<?> target, final String failureReason)
	{
		super(target.getSimpleName() + ": " + failureReason);
	}

	/**
	 * Use this constructor when the value in error could be an unbounded string.
	 *
	 * @param target the class declaring the violated rule.
	 * @param failureReason a helpful description of the error.
	 * @param valueInError a string representation of the value that caused this exception.
	 * 	For safety, this constructor will shorten it to a maximum length of {@value #VALUE_LENGTH_THRESHOLD} in the
	 * 	exception message.
	 */
	public ValidationException(final Class<?> target, final String failureReason, final String valueInError)
	{
		this(target, failureReason + ": " + _abbreviateString(valueInError));
	}

	static final int VALUE_LENGTH_THRESHOLD = 256;
	private static final int _PREFIX_LENGTH = VALUE_LENGTH_THRESHOLD / 2 - 1;
	private static final int _SUFFIX_LENGTH = _PREFIX_LENGTH - 1;

	private static String _abbreviateString(final String valueInError)
	{
		final int length = valueInError.length();
		if (length <= VALUE_LENGTH_THRESHOLD) { return valueInError; }
		return valueInError.substring(0, _PREFIX_LENGTH) + "..." + valueInError.substring(length - _SUFFIX_LENGTH);
	}
}
