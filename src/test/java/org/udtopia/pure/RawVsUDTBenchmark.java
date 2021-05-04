package org.udtopia.pure;

import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.udtopia.BaseBenchmark;
import org.udtopia.Value;

public class RawVsUDTBenchmark extends BaseBenchmark
{
	public static void main(final String[] args) { runBenchmark(args); }

	static final @Value class Count extends PureLong<Count>
	{
		Count(final long raw) { super(raw); }
	}

	private static final int _ZERO_RAW = 0;
	private static final Count _ZERO_UDT = new Count(_ZERO_RAW);

	private long _raw;
	private Count _udt;

	private long[] _rawArray;
	private Count[] _udtArray;

	@Setup(Level.Iteration) public void randomValue()
	{
		_raw = RAND.nextLong();
		_udt = new Count(_raw);

		_rawArray = RAND.longs(10).toArray();
		_udtArray = LongStream.of(_rawArray).mapToObj(Count::new).toArray(Count[]::new);
	}

	@Benchmark public long getRaw() { return _raw; }

	@Benchmark public long getUDT() { return _udt.getAsLong(); }

	@Benchmark public boolean equalsRaw() { return _raw == _ZERO_RAW; }

	@Benchmark public boolean equalsUDT() { return _udt.eq(_ZERO_UDT); }

	@Benchmark public String toStringRaw() { return "hello " + _raw; }

	@Benchmark public String toStringUDT() { return "hello " + _udt; }

	@Benchmark public OptionalLong minRaw() { return LongStream.of(_rawArray).reduce(Math::min); }

	@Benchmark public Optional<Count> minUDT() { return Stream.of(_udtArray).reduce(Count::min); }
}
