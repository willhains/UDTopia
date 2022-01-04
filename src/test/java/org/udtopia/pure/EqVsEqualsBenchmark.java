package org.udtopia.pure;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.udtopia.BaseBenchmark;
import org.udtopia.Value;

public class EqVsEqualsBenchmark extends BaseBenchmark
{
	public static void main(final String[] args) { runBenchmark(args); }

	static final @Value class Quantity extends PureLong<Quantity>
	{
		Quantity(final long raw) { super(raw); }
	}

	@Setup(Level.Iteration) public void generateRandomValues()
	{
		final long qty = RAND.nextLong();
		_quantity1 = new Quantity(qty);
		_quantity2 = new Quantity(qty);
		_quantity3 = new Quantity(qty + 1);
		_quantity4 = _quantity1;
	}

	private Quantity _quantity1, _quantity2, _quantity3, _quantity4;

	@Benchmark public boolean equiv_eq()
	{
		return _quantity1.eq(_quantity2);
	}

	@Benchmark public boolean equiv_equals()
	{
		return _quantity1.equals(_quantity2);
	}

	@Benchmark public boolean diff_eq()
	{
		return _quantity1.eq(_quantity3);
	}

	@Benchmark public boolean diff_equals()
	{
		return _quantity1.equals(_quantity3);
	}

	@Benchmark public boolean same_eq()
	{
		return _quantity1.eq(_quantity1);
	}

	@Benchmark public boolean same_equals()
	{
		return _quantity1.equals(_quantity4);
	}
}
