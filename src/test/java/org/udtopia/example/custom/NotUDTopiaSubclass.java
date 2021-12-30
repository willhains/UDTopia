package org.udtopia.example.custom;

import org.udtopia.Value;
import org.udtopia.rules.DoubleRule;
import org.udtopia.rules.Floor;
import org.udtopia.rules.IntRule;
import org.udtopia.rules.LongRule;
import org.udtopia.rules.StringRule;
import org.udtopia.rules.UpperCase;

@Floor(0) @UpperCase
public final @Value class NotUDTopiaSubclass
{
	public final int f1;
	public final long f2;
	public final double f3;
	public final String f4;

	public NotUDTopiaSubclass(final int v1, final long v2, final double v3, final String v4)
	{
		f1 = IntRule.applyRulesFor(getClass(), v1);
		f2 = LongRule.applyRulesFor(getClass(), v2);
		f3 = DoubleRule.applyRulesFor(getClass(), v3);
		f4 = StringRule.applyRulesFor(getClass(), v4);
	}
}
