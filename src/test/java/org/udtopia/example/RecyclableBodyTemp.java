package org.udtopia.example;

import org.udtopia.Value;
import org.udtopia.recycle.RecyclableDouble;

public final @Value class RecyclableBodyTemp extends RecyclableDouble<RecyclableBodyTemp>
{
	private RecyclableBodyTemp(final double reading) { super(RecyclableBodyTemp::inCelsius, reading); }

	public static RecyclableBodyTemp inCelsius(final double reading)
	{
		return recycle(RecyclableBodyTemp.class, RecyclableBodyTemp::new, reading);
	}
}
