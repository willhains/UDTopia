package org.udtopia.recycle;

import org.openjdk.jmh.annotations.Benchmark;
import org.udtopia.BaseBenchmark;
import org.udtopia.Value;
import org.udtopia.pure.PureDouble;

public class JavaAllocBenchmark extends BaseBenchmark
{
	public static void main(final String[] args) { runBenchmark(args); }

	@SingleProducer
	static final @Value class Temperature extends RecyclableDouble<Temperature>
	{
		private Temperature(final double raw) { super(Temperature::temp, raw); }

		public static Temperature temp(final double raw) { return recycle(Temperature.class, Temperature::new, raw); }
	}

	Temperature temperature = new Temperature(0);

	@Benchmark public Temperature recycleSingleProducer()
	{
		temperature.discard();
		temperature = Temperature.temp(RAND.nextDouble());
		return temperature;
	}

	static final @Value class Humidity extends RecyclableDouble<Humidity>
	{
		private Humidity(final double raw) { super(Humidity::temp, raw); }

		public static Humidity temp(final double raw) { return recycle(Humidity.class, Humidity::new, raw); }
	}

	Humidity humidity = new Humidity(0);

	@Benchmark public Humidity recycleMultiProducer()
	{
		humidity.discard();
		humidity = Humidity.temp(RAND.nextDouble());
		return humidity;
	}

	static final @Value class Quantity extends PureDouble<Quantity>
	{
		Quantity(final double raw) { super(Quantity::new, raw); }
	}

	Quantity quantity = new Quantity(0);

	@Benchmark public Quantity pure()
	{
		quantity = new Quantity(RAND.nextDouble());
		return quantity;
	}

	static final @Value class Height
	{
		private final double _raw;

		Height(final double raw) { _raw = raw; }

		@Override public int hashCode() { return Double.hashCode(_raw); }

		@Override public boolean equals(final Object obj) { return obj instanceof Height && eq((Height) obj); }

		public boolean eq(final Height that) { return this == that || this._raw == that._raw; }

		@Override public String toString() { return Double.toString(_raw); }
	}

	Height height = new Height(0);

	@Benchmark public Height pojo()
	{
		height = new Height(RAND.nextDouble());
		return height;
	}
}
