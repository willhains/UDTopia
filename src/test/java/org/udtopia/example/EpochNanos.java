package org.udtopia.example;

import org.udtopia.Value;
import org.udtopia.pure.PureLong;

public final @Value class EpochNanos extends PureLong<EpochNanos>
{
	public EpochNanos(final long nanosSinceEpoch) { super(EpochNanos::new, nanosSinceEpoch); }
}
