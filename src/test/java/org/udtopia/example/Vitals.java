package org.udtopia.example;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.udtopia.Value;
import org.udtopia.assertion.Assert;
import org.udtopia.recycle.Recyclable;
import org.udtopia.recycle.RecycleBin;

public final @Value class Vitals implements Recyclable
{
	private @Nullable BodyTemp _bbt;
	private @Nullable BloodOxygen _saO2;

	private Vitals(final @Nonnull BodyTemp bbt, final @Nonnull BloodOxygen saO2)
	{
		Assert.notNull(() -> bbt);
		Assert.notNull(() -> saO2);
		_bbt = bbt;
		_saO2 = saO2;
	}

	public static Vitals readings(final BodyTemp bbt, final BloodOxygen saO2)
	{
		return RecycleBin.forClass(Vitals.class).recycle(
			discarded ->
			{
				discarded._bbt = bbt;
				discarded._saO2 = saO2;
			},
			() -> new Vitals(bbt, saO2));
	}

	@Override public boolean isDiscarded()
	{
		return _bbt == null && _saO2 == null;
	}

	@Override public void discard()
	{
		Assert.not(this::isDiscarded, "Attempted to discard twice!");
		_bbt = null;
		_saO2 = null;
	}

	public BodyTemp getBodyTemp()
	{
		Assert.not(this::isDiscarded, "Attempted to access discarded instance!");
		return _bbt;
	}

	public BloodOxygen getOxygenSat()
	{
		Assert.not(this::isDiscarded, "Attempted to access discarded instance!");
		return _saO2;
	}
}
