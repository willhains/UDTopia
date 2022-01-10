package org.udtopia.example;

import org.junit.Test;
import org.udtopia.recycle.RecycleBinSize;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.udtopia.example.Vitals.*;

public class VitalsTest
{
	@Test public void shouldNotBeDiscardedWhenNewlyCreated()
	{
		final Vitals vitals = readings(BodyTemp.inCelsius(36.9), new BloodOxygen(0.994));
		assertThat(vitals.isDiscarded(), is(false));
	}

	@Test public void shouldDiscardAndRecycle()
	{
		final Vitals original = readings(BodyTemp.inCelsius(36.9), new BloodOxygen(0.994));
		Vitals vitals = original;
		for (int i = 0; i < RecycleBinSize.DEFAULT_SIZE; i++)
		{
			vitals.discard();
			vitals = readings(BodyTemp.inCelsius(36.7), new BloodOxygen(0.998));
		}
		assertThat(vitals, is(sameInstance(original)));
	}
}
