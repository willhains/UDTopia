package org.udtopia.assertion;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Utility to control assertions at runtime, for use in unit tests.
 * This is to help minimize apparent missed branch coverage in JaCoCo.
 *
 * At time of writing, the synthetic static block used to initialize classes that contain {@code assert} statements
 * still reports as a missed branch, so coverage thresholds must be set to allow one missed branch per
 * {@code assert}-containing class.
 */
public enum AssertControl
{
	/** Enable assertions. */
	ENABLE,

	/** Disable assertions. */
	DISABLE;

	/**
	 * Enable/disable assertions for the specified class.
	 *
	 * @param c a class with at least one {@code assert} statement.
	 */
	public void forClass(final Class<?> c)
	{
		final boolean enableAssertions = this == ENABLE;

		// Set the assertion status on the class loader, if it hasn't yet been initialized
		c.getClassLoader().setClassAssertionStatus(c.getName(), enableAssertions);

		// Set the synthetic static field that controls assertions for the class
		try
		{
			final Field assertionsDisabled = c.getDeclaredField("$assertionsDisabled");
			assertionsDisabled.setAccessible(true);
			final Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(assertionsDisabled, assertionsDisabled.getModifiers() & ~Modifier.FINAL);
			assertionsDisabled.set(c, !enableAssertions);
		}
		catch (final NoSuchFieldException | IllegalAccessException ignored) { }
	}
}
