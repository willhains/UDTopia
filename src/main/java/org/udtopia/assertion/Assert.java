package org.udtopia.assertion;

import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.udtopia.Value;

/**
 * Encapsulates the Java {@code assert} statement, to control its behaviour in unit tests.
 *
 * In production code, it converts {@code assert} into a method call with similar performance characteristics.
 * In testing, code coverage tools can exclude this class to avoid reporting spurious missed branches.
 */
public @Value interface Assert
{
	/**
	 * @return {@code true} if assertions done via this class will take effect.
	 */
	static boolean isEnabled() { return Assert.class.desiredAssertionStatus(); }

	/**
	 * Assert that the check condition is {@code true}.
	 *
	 * @param check a lambda or method reference returning a boolean result.
	 */
	static void that(final BooleanSupplier check) { assert check.getAsBoolean(); }

	/**
	 * Assert that the check condition is {@code true}.
	 *
	 * @param check a lambda or method reference returning a boolean result.
	 * @param error the message to throw in an assertion error, when the check fails.
	 */
	static void that(final BooleanSupplier check, final String error) { assert check.getAsBoolean() : error; }

	/**
	 * Assert that the check condition is {@code false}.
	 *
	 * @param check a lambda or method reference returning a boolean result.
	 */
	static void not(final BooleanSupplier check) { assert !check.getAsBoolean(); }

	/**
	 * Assert that the check condition is {@code false}.
	 *
	 * @param check a lambda or method reference returning a boolean result.
	 * @param error the message to throw in an assertion error, when the check fails.
	 */
	static void not(final BooleanSupplier check, final String error) { assert !check.getAsBoolean() : error; }

	/**
	 * Assert that an object reference is not null.
	 *
	 * @param object an object reference that should not be null.
	 */
	static void notNull(final Supplier<?> object) { assert object.get() != null; }

	/**
	 * Assert that an object reference is not null.
	 *
	 * @param object an object reference that should not be null.
	 * @param error the message to throw in an assertion error, when the check fails.
	 */
	static void notNull(final Supplier<?> object, final String error) { assert object.get() != null : error; }

	/**
	 * Perform a task only if assertions are active.
	 *
	 * @param task the task that should not run in production.
	 */
	static void debug(final Runnable task)
	{
		that(() ->
		{
			task.run();
			return true;
		});
	}

	/**
	 * Assert that a block of code never throws any exception.
	 *
	 * @param thrownBy a lambda or method reference that the compiler thinks will throw an exception.
	 */
	static void noException(final ThrowingRunnable thrownBy)
	{
		try { thrownBy.run(); }
		catch (final Exception e) { assert false : e.getMessage(); }
	}

	/**
	 * Assert that a block of code never throws any exception.
	 *
	 * @param thrownBy a lambda or method reference that the compiler thinks will throw an exception.
	 * @param <Return> the return value.
	 * @return the return value of the lambda.
	 */
	static <Return> @Nullable Return noException(final Callable<@Nullable Return> thrownBy)
	{
		try { return thrownBy.call(); }
		catch (final Exception e) { assert false : e.getMessage(); }
		return null;
	}

	/** A task that may throw an exception. */
	@FunctionalInterface interface ThrowingRunnable
	{
		/**
		 * Perform the task.
		 *
		 * @throws Exception for any reason.
		 */
		@SuppressWarnings("RedundantThrows")
		void run() throws Exception;
	}
}
