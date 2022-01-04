package org.udtopia.example;

import java.time.LocalDate;
import org.udtopia.Value;
import org.udtopia.pure.PureValue;

public final @Value class ExpiryDate extends PureValue<LocalDate, ExpiryDate>
{
	public ExpiryDate(final LocalDate expiry) { super(expiry); }
}
