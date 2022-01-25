package org.udtopia.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import org.udtopia.Value;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static org.udtopia.rules.ApplyRuleWhen.*;

/**
 * Validate that string values are encodable as the specified character set.
 */
@Documented
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface EncodableAs
{
	/**
	 * @return the character set name. The {@link Rule} throws {@link IllegalArgumentException} if invalid.
	 * @see Charset
	 */
	String value();

	/** @return when to apply this rule. */
	ApplyRuleWhen when() default ALWAYS;

	/** Rule to apply {@link EncodableAs} to string values. */
	final @Value class Rule implements StringValidator
	{
		static final int STRING_LENGTH_THRESHOLD = 24;

		/**
		 * Build a rule from an annotation.
		 *
		 * @param annotation an {@link EncodableAs} annotation.
		 * @throws IllegalArgumentException subclasses if the provided charset name is invalid.
		 */
		public Rule(final EncodableAs annotation) { this(annotation.value()); }

		private final Charset _allowedCharset;
		private final ThreadLocal<CharsetEncoder> _encoder;

		Rule(final String charsetName)
		{
			_allowedCharset = Charset.forName(charsetName);
			_encoder = ThreadLocal.withInitial(_allowedCharset::newEncoder);
		}

		@Override public void validate(final Class<?> target, final String value)
		{
			if (_encoder.get().canEncode(value)) { return; }
			throw new ValidationException(target, "Text cannot be encoded as " + _allowedCharset, value);
		}

		@Override public String toString()
		{
			return "@" + EncodableAs.class.getSimpleName() + "(" + _allowedCharset + ")";
		}
	}
}
