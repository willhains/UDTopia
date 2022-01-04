package org.udtopia.example;

import org.udtopia.Value;
import org.udtopia.pure.PureDouble;

public final @Value class BloodOxygen extends PureDouble<BloodOxygen>
{
	public static final double MIN_BLOOD_O2 = 0.85, MAX_BLOOD_O2 = 1.0;

	public BloodOxygen(final double reading)
	{
		super(BloodOxygen::new, reading);
		if (reading < MIN_BLOOD_O2 || reading > MAX_BLOOD_O2)
		{
			throw new IllegalArgumentException("Invalid blood oxygen: " + reading);
		}
	}
}
