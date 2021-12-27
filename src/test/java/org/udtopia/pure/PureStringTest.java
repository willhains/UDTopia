package org.udtopia.pure;

import java.util.Iterator;
import java.util.stream.BaseStream;
import org.junit.Test;
import org.udtopia.Value;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class PureStringTest
{
	static final @Value class UserId extends PureString<UserId>
	{
		UserId(final String rawValue) { super(rawValue); }
	}

	@Test(expected = AssertionError.class) public void shouldTrapNullUnderlyingValue()
	{
		new UserId(null);
	}

	@Test public void shouldReturnRawValueAfterConstruction()
	{
		assertThat(new UserId("").get(), is(equalTo("")));

		final UserId x = new UserId("a");
		assertThat(x.get(), is(equalTo("a")));
		assertThat(x.get(), is(equalTo("a")));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void shouldAlwaysBeUnequalToNull()
	{
		final UserId x = new UserId("b");
		assertThat(x.equals(null), is(false));

		final UserId y = new UserId("");
		assertThat(y.equals(null), is(false));
	}

	static final @Value class UserId2 extends PureString<UserId2>
	{
		UserId2(final String x) { super(x); }
	}

	@Test public void shouldAlwaysBeUnequalToDifferentClass()
	{
		final UserId x = new UserId("c");
		final UserId2 y = new UserId2("c");
		assertThat(x, is(not(equalTo(y))));
	}

	@Test(expected = AssertionError.class) public void shouldTrapNullEq()
	{
		final UserId x = new UserId("");
		x.eq(null);
	}

	@Test public void shouldAlwaysBeEqualWithSameRaw()
	{
		final String s = "d";
		final UserId x = new UserId(s);
		final UserId y = new UserId(s);
		assertThat(x, is(equalTo(y)));
		assertThat(x.equals(y), is(true));
		assertThat(x.eq(y), is(true));
	}

	@Test public void shouldBeReflexive()
	{
		final UserId x = new UserId("e");
		assertThat(x, is(equalTo(x)));
		assertThat(x.eq(x), is(true));
	}

	@Test public void shouldBeSymmetric()
	{
		final UserId x = new UserId("f");
		final UserId y = new UserId("f");
		assertThat(x, is(equalTo(y)));
		assertThat(x.eq(y), is(true));
		assertThat(y, is(equalTo(x)));
		assertThat(y.eq(x), is(true));

		final UserId z = new UserId("g");
		assertThat(x, is(not(equalTo(z))));
		assertThat(x.eq(z), is(false));
		assertThat(z, is(not(equalTo(x))));
		assertThat(z.eq(x), is(false));
	}

	@Test public void shouldBeTransitive()
	{
		final UserId x = new UserId("h");
		final UserId y = new UserId("h");
		final UserId z = new UserId("h");
		assertThat(x, is(equalTo(y)));
		assertThat(x.eq(y), is(true));
		assertThat(y, is(equalTo(z)));
		assertThat(y.eq(z), is(true));
		assertThat(x, is(equalTo(z)));
		assertThat(x.eq(z), is(true));

		final UserId w = new UserId("i");
		assertThat(w, is(not(equalTo(y))));
		assertThat(w.eq(y), is(false));
		assertThat(w, is(not(equalTo(z))));
		assertThat(w.eq(z), is(false));
	}

	@Test public void shouldBeConsistent()
	{
		final UserId x = new UserId("j");
		final UserId y = new UserId("j");
		final UserId z = new UserId("k");
		assertThat(x, is(equalTo(y)));
		assertThat(x.eq(y), is(true));
		assertThat(x, is(not(equalTo(z))));
		assertThat(x.eq(z), is(false));
		assertThat(x, is(equalTo(y)));
		assertThat(x.eq(y), is(true));
		assertThat(x, is(not(equalTo(z))));
		assertThat(x.eq(z), is(false));

		final int xHash1 = x.hashCode();
		final int xHash2 = x.hashCode();
		assertThat(xHash1, is(equalTo(xHash2)));
	}

	@Test public void shouldComputeSameHashCode()
	{
		final UserId x = new UserId("l");
		final UserId y = new UserId("l");
		assertThat(x, is(equalTo(y)));
		assertThat(x.eq(y), is(true));

		final int xHash = x.hashCode();
		final int yHash = y.hashCode();
		assertThat(xHash, is(equalTo(yHash)));
	}

	@Test public void shouldComputeDifferentHashCodes()
	{
		final UserId x = new UserId("m");
		final UserId y = new UserId("n");
		assertThat(x, is(not(equalTo(y))));
		assertThat(x.eq(y), is(false));

		final int xHash = x.hashCode();
		final int yHash = y.hashCode();
		assertThat(xHash, is(not(equalTo(yHash))));
	}

	@Test public void shouldGenerateSameStringAsUnderlying()
	{
		final UserId x = new UserId("o");
		assertThat(x.toString(), is(equalTo("o")));
	}

	static class FlagTest extends PureString<FlagTest>
	{
		FlagTest(final String s) { super(s); }
	}

	@Test public void shouldBeSameCharSequenceAsRaw()
	{
		final String string = "This is the Australian flag: \uD83C\uDDE6\uD83C\uDDFA";
		final FlagTest udtString = new FlagTest(string);

		assertEquals(udtString.chars(), string.chars());
		assertEquals(udtString.codePoints(), string.codePoints());
		assertThat(udtString.length(), is(equalTo(string.length())));
		assertThat(udtString.charAt(29), is(equalTo(string.charAt(29))));
		assertThat(udtString.subSequence(23, 33).get(), is(equalTo(string.subSequence(23, 33))));
	}

	@Test public void shouldBeEmpty() { assertThat(new UserId("").isEmpty(), is(true)); }

	@Test public void shouldNotBeEmpty() { assertThat(new UserId("x").isEmpty(), is(false)); }

	static <T, S extends BaseStream<T, S>> void assertEquals(final BaseStream<T, S> s1, final BaseStream<T, S> s2)
	{
		final Iterator<T> i1 = s1.iterator(), i2 = s2.iterator();
		while (i1.hasNext() && i2.hasNext()) { assertThat(i1.next(), is(equalTo(i2.next()))); }
		assertThat(i1.hasNext(), is(false));
		assertThat(i2.hasNext(), is(false));
	}

	@Test public void shouldCompareAsLarger()
	{
		final UserId x = new UserId("p");
		final UserId y = new UserId("q");
		assertThat(y, is(greaterThan(x)));
		assertThat(y.compareTo(x), is(greaterThan(0)));
		assertThat(y.compareTo("p"), is(greaterThan(0)));
		assertThat(y.compareTo(new UserId2("p")), is(greaterThan(0)));
	}

	@Test public void shouldCompareAsSmaller()
	{
		final UserId x = new UserId("r");
		final UserId y = new UserId("s");
		assertThat(x, is(lessThan(y)));
		assertThat(x.compareTo(y), is(lessThan(0)));
		assertThat(x.compareTo("s"), is(lessThan(0)));
		assertThat(x.compareTo(new UserId2("s")), is(lessThan(0)));
	}

	@Test public void shouldCompareAsEqual()
	{
		final UserId x = new UserId("t");
		final UserId y = new UserId("t");
		assertThat(y, is(equalTo(x)));
		assertThat(x.compareTo(y), is(equalTo(0)));
		assertThat(x.compareTo("t"), is(equalTo(0)));
		assertThat(x.compareTo(new UserId2("t")), is(equalTo(0)));
	}
}
