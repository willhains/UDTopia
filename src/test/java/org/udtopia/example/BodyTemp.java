package org.udtopia.example;

import org.udtopia.Value;
import org.udtopia.pure.PureDouble;
import org.udtopia.rules.Max;
import org.udtopia.rules.Min;

import static org.udtopia.example.BodyTemp.*;
import static org.udtopia.rules.ApplyRuleWhen.*;

@Min(value = MIN_BODY_TEMP, when = ALWAYS) @Max(MAX_BODY_TEMP)
public final @Value class BodyTemp extends PureDouble<BodyTemp>
{
	public static final double MIN_BODY_TEMP = 30.0, MAX_BODY_TEMP = 42.0;

	private BodyTemp(final double reading) { super(BodyTemp::inCelsius, reading); }

	public static BodyTemp inCelsius(final double reading) { return new BodyTemp(reading); }

	public static BodyTemp inFahrenheit(final double reading) { return inCelsius((reading - 32.0) / 1.8); }

	public static final double FEVER_THRESHOLD = 37.6;

	public boolean isFever() { return getAsDouble() > FEVER_THRESHOLD; }

	public double getAsFahrenheit() { return getAsDouble() * 1.8 + 32.0; }
}
