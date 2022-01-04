package org.udtopia.example;

import java.awt.Point;
import org.udtopia.Value;
import org.udtopia.pure.PureValue;

public final @Value class MousePosition extends PureValue<Point, MousePosition>
{
	public MousePosition(final Point point)
	{
		super(point, p -> new Point(p.x, p.y));
	}
}
