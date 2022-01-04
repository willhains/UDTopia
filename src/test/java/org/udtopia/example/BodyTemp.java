package org.udtopia.example;

import org.udtopia.Value;
import org.udtopia.pure.PureDouble;

public final @Value class BodyTemp extends PureDouble<BodyTemp>
{
	public static final double MIN_BODY_TEMP = 30.0, MAX_BODY_TEMP = 42.0;

	public BodyTemp(final double reading)
	{
		super(reading);
		if (reading < MIN_BODY_TEMP || reading > MAX_BODY_TEMP)
		{
			throw new IllegalArgumentException("Invalid body temp: " + reading);
		}
	}

	public static final double FEVER_THRESHOLD = 37.6;

	public boolean isFever() { return getAsDouble() > FEVER_THRESHOLD; }

	public double getAsFahrenheit() { return getAsDouble() * 1.8 + 32.0; }
}
