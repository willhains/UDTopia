package org.udtopia.rules;

/**
 * An unrecoverable fault in the initialisation of rules.
 */
class RulesError extends Error
{
	private static final long serialVersionUID = 513027496412055938L;

	RulesError(final String message) { super(message); }

	RulesError(final Throwable cause, final String message) { super(message, cause); }
}
