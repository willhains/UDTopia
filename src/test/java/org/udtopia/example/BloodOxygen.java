package org.udtopia.example;

import org.udtopia.Value;
import org.udtopia.pure.PureDouble;
import org.udtopia.rules.Max;
import org.udtopia.rules.Min;

import static org.udtopia.example.BloodOxygen.*;

@Min(MIN_BLOOD_O2) @Max(MAX_BLOOD_O2)
public final @Value class BloodOxygen extends PureDouble<BloodOxygen>
{
	public static final double MIN_BLOOD_O2 = 0.85, MAX_BLOOD_O2 = 1.0;

	public BloodOxygen(final double reading) { super(BloodOxygen::new, reading); }
}
