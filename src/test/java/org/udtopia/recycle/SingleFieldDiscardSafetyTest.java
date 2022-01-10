package org.udtopia.recycle;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.udtopia.Value;

import static java.lang.String.*;
import static java.lang.System.*;
import static java.util.Collections.*;
import static java.util.concurrent.TimeUnit.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.*;

public interface SingleFieldDiscardSafetyTest
{
	long TEST_DURATION_MINUTES = 1;
	int CONSUMERS = Runtime.getRuntime().availableProcessors() * 7;

	static void main(final String[] args)
	{
		final long stopTime = currentTimeMillis() + MINUTES.toMillis(TEST_DURATION_MINUTES);

		final QuantityProducer producer = new QuantityProducer();
		final List<QuantityConsumer> consumers = generate(QuantityConsumer::new).limit(CONSUMERS).collect(toList());

		while (currentTimeMillis() < stopTime)
		{
			producer.update();
			shuffle(consumers);
			consumers.forEach(consumer -> consumer.accept(producer.get()));
		}

		final double producerTotal = producer.finish();
		exit(consumers.stream()
			.map(quantityConsumer -> quantityConsumer.finish(producerTotal))
			.mapToInt(match -> match ? 0 : 1)
			.sum());
	}

	@RecycleBinSize(4) @SingleProducer final @Value class Quantity extends RecyclableDouble<Quantity>
	{
		private Quantity(final double raw) { super(Quantity::qty, raw); }

		static Quantity qty(final double raw) { return recycle(Quantity.class, Quantity::new, raw); }
	}

	final class QuantityProducer implements Supplier<Quantity>
	{
		private final Random _random = new Random();
		private double _currentValue;
		private double _total;
		private long _count;

		public void update()
		{
			_count++;
			_currentValue = _random.nextDouble();
			_total += _currentValue;
		}

		@Override public Quantity get() { return Quantity.qty(_currentValue); }

		@Override public String toString()
		{
			final RecycleBin<Quantity> recycleBin = RecycleBin.forClass(Quantity.class);
			return format("producer: (%d) %s -- %s", _count, new BigDecimal(_total), recycleBin);
		}

		public double finish()
		{
			out.println(this + " <-- final value");
			return _total;
		}
	}

	final class QuantityConsumer implements Consumer<Quantity>
	{
		private double _total;
		private long _count;

		private final ExecutorService _executor = Executors.newSingleThreadExecutor();

		@Override public void accept(final Quantity qty)
		{
			_executor.submit(() ->
			{
				_count++;
				final double value = qty.getAsDouble(); // DO NOT inline!
				qty.discard();
				_total += value;
			});
		}

		/** @return {@code true} if the consumer's total matches the producer's; {@code false} if there's a diff. */
		public boolean finish(final double producerTotal)
		{
			final boolean totalsMatch = _total == producerTotal;
			_executor.submit(() -> out.println(totalsMatch ? this : this + " <-- DIFF!"));
			_executor.shutdown();
			try { if (!_executor.awaitTermination(10, SECONDS)) { err.println("Timed out waiting for termination"); } }
			catch (final InterruptedException e)
			{
				err.println("Unable to shut down a consumer!");
				Thread.currentThread().interrupt();
			}
			return totalsMatch;
		}

		@Override public String toString() { return format("consumer: (%d) %s", _count, new BigDecimal(_total)); }
	}
}
