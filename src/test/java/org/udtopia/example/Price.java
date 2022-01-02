package org.udtopia.example;

import org.udtopia.Value;
import org.udtopia.pure.PureDouble;

public final @Value class Price extends PureDouble<Price>
{
	public Price(final double px) { super(Price::new, px); }
}
