package org.udtopia.example.custom;

import java.lang.annotation.Retention;
import org.udtopia.Value;
import org.udtopia.rules.StringNormalizer;

import static java.lang.annotation.RetentionPolicy.*;

// Custom rule to replace "cool" with "kewl"
@Retention(RUNTIME)
public @interface Kewl
{
	final @Value class Rule implements StringNormalizer
	{
		public Rule(final Kewl annotation) { }

		@Override public String normalize(final String value)
		{
			return value.replace("cool", "kewl");
		}
	}
}
