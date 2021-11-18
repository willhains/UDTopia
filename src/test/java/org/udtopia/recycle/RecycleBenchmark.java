package org.udtopia.recycle;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.udtopia.BaseBenchmark;
import org.udtopia.Value;

public class RecycleBenchmark extends BaseBenchmark
{
	public static void main(final String[] args) { runBenchmark(args); }

	@SingleProducer
	static final @Value class Quantity extends RecyclableDouble<Quantity>
	{
		private Quantity(final double raw) { super(Quantity::qty, raw); }

		static Quantity qty(final double raw) { return recycle(Quantity.class, Quantity::new, raw); }
	}

	@Param({"0.00", "0.50", "0.95", "1.00"}) double discardRate;
	Quantity previous = Quantity.qty(0);

	@Benchmark public Quantity withRandomDiscard()
	{
		final double random = RAND.nextDouble();
		final Quantity qty = Quantity.qty(random);
		if (random <= discardRate) { previous.discard(); }
		previous = qty;
		return qty;
	}
}
