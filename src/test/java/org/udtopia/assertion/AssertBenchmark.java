package org.udtopia.assertion;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.udtopia.BaseBenchmark;

public class AssertBenchmark extends BaseBenchmark
{
	public static void main(final String[] args) { runBenchmark(args); }

	static final class WithJavaAssert
	{
		@SuppressWarnings("FieldCanBeLocal")
		private final Object _val;

		WithJavaAssert(final Object val)
		{
			assert val != null : "val must not be null";
			_val = val;
		}
	}

	static final class WithUDTopiaAssert
	{
		@SuppressWarnings("FieldCanBeLocal")
		private final Object _val;

		WithUDTopiaAssert(final Object val)
		{
			Assert.notNull(() -> val, "val must not be null");
			_val = val;
		}
	}

	static final class WithNoAssert
	{
		@SuppressWarnings("FieldCanBeLocal")
		private final Object _val;

		WithNoAssert(final Object val)
		{
			_val = val;
		}
	}

	private Object _randomVal;
	private final Object _result = new WithNoAssert("");

	@Setup(Level.Iteration) public void generateRandomValue() { _randomVal = RAND_STR.get(); }

	@Benchmark public Object withJavaAssert() { return new WithJavaAssert(_result.toString() + _randomVal); }

	@Benchmark public Object withUDTopiaAssert() { return new WithUDTopiaAssert(_result.toString() + _randomVal); }

	@Benchmark public Object withNoAssert() { return new WithNoAssert(_result.toString() + _randomVal); }
}
