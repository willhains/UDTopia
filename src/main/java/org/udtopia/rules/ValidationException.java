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
}
