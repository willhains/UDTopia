package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import org.udtopia.Value;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static org.udtopia.rules.Truncate.TruncateFrom.*;

/**
 * Truncate values to a specified maximum length.
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface Truncate
{
	/** @return the maximum length. */
	int toLength();

	/** @return the side to truncate from. */
	TruncateFrom from() default RIGHT;

	/** @return character(s) to indicate length overflow. */
	String ellipsis() default "";

	/** Rule to apply {@link Truncate} to string values. */
	final @Value class Rule implements StringNormalizer
	{
		private final int _length;
		private final UnaryOperator<String> _truncator;

		/**
		 * Build a Truncate rule from an annotation.
		 *
		 * @param annotation a {@link Truncate} annotation.
		 */
		public Rule(final Truncate annotation)
		{
			this(annotation.toLength(), annotation.from(), annotation.ellipsis());
		}

		Rule(final int length, final TruncateFrom side, final String ellipsis)
		{
			if (length <= 0) { throw new IllegalArgumentException("Invalid max length: " + length); }
			if (length < ellipsis.length())
			{
				throw new IllegalArgumentException(
					"ellipsis '" + ellipsis + "' is longer than max length (" + length + ")");
			}

			_length = length;
			_truncator = side.truncation(length - ellipsis.length(), ellipsis);
		}

		@Override public String normalize(final String value)
		{
			return value.length() > _length ? _truncator.apply(value) : value;
		}
	}

	/** Which side to remove characters from to achieve the desired maximum length. */
	enum TruncateFrom
	{
		/** Example: "hello world" truncated to 8 chars becomes "hello wo". */
		RIGHT((effectiveLength, ellipsis) -> value -> value.substring(0, effectiveLength) + ellipsis),

		/** Example: "hello world" truncated to 8 chars becomes "hellorld". */
		MIDDLE((effectiveLength, ellipsis) ->
		{
			final int beginningLength = effectiveLength / 2;
			final int endLength = effectiveLength - beginningLength;
			return value ->
			{
				final String beginning = value.substring(0, beginningLength);
				final String end = value.substring(value.length() - endLength);
				return beginning + ellipsis + end;
			};
		}),

		/** Example: "hello world" truncated to 8 chars becomes "lo world". */
		LEFT((effectiveLength, ellipsis) -> value -> ellipsis + value.substring(value.length() - effectiveLength));

		private final BiFunction<Integer, String, UnaryOperator<String>> _truncationFactory;

		TruncateFrom(final BiFunction<Integer, String, UnaryOperator<String>> truncationFactory)
		{
			_truncationFactory = truncationFactory;
		}

		UnaryOperator<String> truncation(final int length, final String ellipsis)
		{
			return _truncationFactory.apply(length, ellipsis);
		}
	}
}
