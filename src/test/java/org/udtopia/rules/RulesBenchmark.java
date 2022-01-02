package org.udtopia.rules;

import org.openjdk.jmh.annotations.Benchmark;
import org.udtopia.BaseBenchmark;
import org.udtopia.Value;
import org.udtopia.pure.PureDouble;
import org.udtopia.pure.PureString;

public class RulesBenchmark extends BaseBenchmark
{
	public static void main(final String[] args) { runBenchmark(args); }

	static final @Value class Pojo
	{
		@SuppressWarnings("FieldCanBeLocal")
		private final double _raw;

		Pojo(final double raw) { _raw = raw; }
	}

	@Benchmark public static double baselineRandom() { return RAND.nextDouble(); }

	@Benchmark public static Pojo baselinePojoNew() { return new Pojo(RAND.nextDouble()); }

	static final @Value class NoRuleUDT extends PureDouble<NoRuleUDT>
	{
		NoRuleUDT(final double raw) { super(NoRuleUDT::new, raw); }
	}

	@Benchmark public static NoRuleUDT udtNoRules() { return new NoRuleUDT(RAND.nextDouble()); }

	@Ceiling(0.5)
	static final @Value class CeilingUDT extends PureDouble<CeilingUDT>
	{
		CeilingUDT(final double raw) { super(CeilingUDT::new, raw); }
	}

	@Benchmark public static CeilingUDT ceilingUDT() { return new CeilingUDT(RAND.nextDouble()); }

	@Floor(0.5)
	static final @Value class FloorUDT extends PureDouble<FloorUDT>
	{
		FloorUDT(final double raw) { super(FloorUDT::new, raw); }
	}

	@Benchmark public static FloorUDT floorUDT() { return new FloorUDT(RAND.nextDouble()); }

	@Max(1.1)
	static final @Value class MaxUDT extends PureDouble<MaxUDT>
	{
		MaxUDT(final double raw) { super(MaxUDT::new, raw); }
	}

	@Benchmark public static MaxUDT maxUDT() { return new MaxUDT(RAND.nextDouble()); }

	@MultipleOf(5)
	static final @Value class MultipleOfUDT extends PureDouble<MultipleOfUDT>
	{
		MultipleOfUDT(final double raw) { super(MultipleOfUDT::new, raw); }
	}

	@Benchmark public static MultipleOfUDT multipleOfUDT() { return new MultipleOfUDT(RAND.nextLong() * 5); }

	@GreaterThan(-0.1)
	static final @Value class GreaterThanUDT extends PureDouble<GreaterThanUDT>
	{
		GreaterThanUDT(final double raw) { super(GreaterThanUDT::new, raw); }
	}

	@Benchmark public static GreaterThanUDT greaterThanUDT() { return new GreaterThanUDT(RAND.nextDouble()); }

	@Min(-0.1)
	static final @Value class MinUDT extends PureDouble<MinUDT>
	{
		MinUDT(final double raw) { super(MinUDT::new, raw); }
	}

	@Benchmark public static MinUDT minUDT() { return new MinUDT(RAND.nextDouble()); }

	@Round(toNearest = 0.5)
	static final @Value class RoundUDT extends PureDouble<RoundUDT>
	{
		RoundUDT(final double raw) { super(RoundUDT::new, raw); }
	}

	@Benchmark public static RoundUDT roundUDT() { return new RoundUDT(RAND.nextDouble()); }

	@LessThan(1.1)
	static final @Value class LessThanUDT extends PureDouble<LessThanUDT>
	{
		LessThanUDT(final double raw) { super(LessThanUDT::new, raw); }
	}

	@Benchmark public static LessThanUDT lessThanUDT() { return new LessThanUDT(RAND.nextDouble()); }

	@Chars(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~")
	static final @Value class CharsUDT extends PureString<CharsUDT>
	{
		CharsUDT(final String raw) { super(CharsUDT::new, raw); }
	}

	@Benchmark public static CharsUDT charsUDT() { return new CharsUDT(RAND_STR.get()); }

	@NotChars("\u0001\u0002")
	static final @Value class NotCharsUDT extends PureString<NotCharsUDT>
	{
		NotCharsUDT(final String raw) { super(NotCharsUDT::new, raw); }
	}

	@Benchmark public static NotCharsUDT notCharsUDT() { return new NotCharsUDT(RAND_STR.get()); }

	@LowerCase
	static final @Value class LowerCaseUDT extends PureString<LowerCaseUDT>
	{
		LowerCaseUDT(final String raw) { super(LowerCaseUDT::new, raw); }
	}

	@Benchmark public static LowerCaseUDT lowerCaseUDT() { return new LowerCaseUDT(RAND_STR.get()); }

	@Replace(pattern = "ab", with = "AB")
	static final @Value class ReplaceUDT extends PureString<ReplaceUDT>
	{
		ReplaceUDT(final String raw) { super(ReplaceUDT::new, raw); }
	}

	@Benchmark public static ReplaceUDT replaceUDT() { return new ReplaceUDT(RAND_STR.get()); }

	@UpperCase
	static final @Value class UpperCaseUDT extends PureString<UpperCaseUDT>
	{
		UpperCaseUDT(final String raw) { super(UpperCaseUDT::new, raw); }
	}

	@Benchmark public static UpperCaseUDT upperCaseUDT() { return new UpperCaseUDT(RAND_STR.get()); }

	@Trim
	static final @Value class TrimUDT extends PureString<TrimUDT>
	{
		TrimUDT(final String raw) { super(TrimUDT::new, raw); }
	}

	@Benchmark public static TrimUDT trimUDT() { return new TrimUDT(" " + RAND_STR.get()); }

	@Matching("[^ -~]+")
	static final @Value class MatchingUDT extends PureString<MatchingUDT>
	{
		MatchingUDT(final String raw) { super(MatchingUDT::new, raw); }
	}

	@Benchmark public static MatchingUDT matchingUDT() { return new MatchingUDT(RAND_STR.get()); }

	@NotMatching("xyz")
	static final @Value class NotMatchingUDT extends PureString<NotMatchingUDT>
	{
		NotMatchingUDT(final String raw) { super(NotMatchingUDT::new, raw); }
	}

	@Benchmark public static NotMatchingUDT notMatchingUDT() { return new NotMatchingUDT(RAND_STR.get()); }
}
