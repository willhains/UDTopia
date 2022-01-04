package org.udtopia.example;

import org.udtopia.Value;
import org.udtopia.pure.PureInt;

public final @Value class DayCount extends PureInt<DayCount>
{
	public DayCount(final int days) { super(DayCount::new, days); }
}
