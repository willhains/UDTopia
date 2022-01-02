package org.udtopia.example;

import org.junit.Test;
import org.udtopia.Value;
import org.udtopia.example.custom.Kewl;
import org.udtopia.example.custom.PowerOf;
import org.udtopia.pure.PureLong;
import org.udtopia.pure.PureString;
import org.udtopia.rules.ValidationException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class CustomRuleTest
{
	@Kewl
	public static final @Value class KewlString extends PureString<KewlString>
	{
		KewlString(final String value) { super(KewlString::new, value); }
	}

	@Test public void shouldApplyCustomNormalizerRule()
	{
		assertThat(new KewlString("UDTopia is cool").get(), is("UDTopia is kewl"));
	}

	@PowerOf(3)
	public static final @Value class PowerOfThree extends PureLong<PowerOfThree>
	{
		PowerOfThree(final long value) { super(PowerOfThree::new, value); }
	}

	@Test(expected = ValidationException.class) public void shouldFailCustomValidatorRule()
	{
		new PowerOfThree(28L);
	}

	@Test public void shouldPassCustomValidatorRule()
	{
		assertThat(new PowerOfThree(27L).getAsLong(), is(27L));
	}
}
