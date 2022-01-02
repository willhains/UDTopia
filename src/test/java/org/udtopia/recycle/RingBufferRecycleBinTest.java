package org.udtopia.recycle;

import org.junit.Test;
import org.udtopia.Value;
import org.udtopia.assertion.Assert;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class RingBufferRecycleBinTest
{
	private static final int _SIZE = 4;

	@RecycleBinSize(_SIZE)
	static final @Value class Phone implements Recyclable
	{
		private String _areaCode;
		private int _number;

		private Phone(final String areaCode, final int number)
		{
			_areaCode = areaCode;
			_number = number;
		}

		public String get()
		{
			assert !isDiscarded();
			return _areaCode + "-" + _number;
		}

		@Override public boolean isDiscarded() { return _areaCode == null && _number < 1; }

		@SuppressWarnings("AssignmentToNull")
		@Override public void discard()
		{
			_areaCode = null;
			_number = 0;
		}

		public static Phone number(final String areaCode, final int number)
		{
			Assert.notNull(() -> areaCode, "null is not allowed for areaCode");
			return RecycleBin.forClass(Phone.class).recycle(
				phone ->
				{
					phone._areaCode = areaCode;
					phone._number = number;
				},
				() -> new Phone(areaCode, number));
		}
	}

	@Test public void shouldRecycleInstance()
	{
		final Phone p1 = Phone.number("010", 11111111);
		assertThat(p1.get(), is("010-11111111"));
		p1.discard();
		for (int i = 0; i < _SIZE - 1; i++)
		{
			Phone.number("xxx", 0);
		}
		final Phone p2 = Phone.number("020", 22222222);
		assertThat(p2.get(), is("020-22222222"));
		assertThat(p1, is(sameInstance(p2)));
		p2.discard();
	}

	@Test public void shouldNotRecycleInstance()
	{
		final Phone p1 = Phone.number("040", 33333333);
		for (int i = 0; i < _SIZE - 1; i++)
		{
			Phone.number("xxx", 0);
		}
		final Phone p2 = Phone.number("040", 44444444);
		assertThat(p2.get(), is("040-44444444"));
		assertThat(p1, is(not(sameInstance(p2))));
		p2.discard();
	}

	static class Dummy implements Recyclable
	{
		@Override public void discard() { }

		@Override public boolean isDiscarded() { return true; }
	}

	@Test public void shouldSizeBinFromAnnotation()
	{
		@RecycleBinSize(4) class A extends Dummy { }
		@RecycleBinSize(8) class B extends Dummy { }
		final RecycleBin<A> bin1 = RecycleBin.forClass(A.class);
		final RecycleBin<B> bin2 = RecycleBin.forClass(B.class);
		assertThat(bin1.toString(), containsString("4"));
		assertThat(bin2.toString(), containsString("8"));
	}

	@Test public void shouldUseSameRecycleBinForSameClass()
	{
		final RecycleBin<Phone> bin1 = RecycleBin.forClass(Phone.class);
		final RecycleBin<Phone> bin2 = RecycleBin.forClass(Phone.class);
		assertThat(bin1, is(sameInstance(bin2)));
	}

	@Test public void shouldUseDifferentRecycleBinForDifferentClass()
	{
		final RecycleBin<Phone> bin1 = RecycleBin.forClass(Phone.class);
		final RecycleBin<Dummy> bin2 = RecycleBin.forClass(Dummy.class);
		assertThat(bin1, is(not(sameInstance(bin2))));
	}

	@Test public void dummyShouldNeverAppearDiscarded()
	{
		RingBufferRecycleBin.DUMMY.discard();
		assertThat(RingBufferRecycleBin.DUMMY.isDiscarded(), is(false));
	}
}
