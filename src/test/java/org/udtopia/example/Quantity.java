package org.udtopia.example;

import org.udtopia.Value;
import org.udtopia.pure.PureInt;

public final @Value class Quantity extends PureInt<Quantity>
{
	public Quantity(final int qty) { super(Quantity::new, qty); }
}
