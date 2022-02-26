package org.udtopia.recycle;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.udtopia.Value;
import org.udtopia.assertion.Assert;

import static java.lang.Double.*;
import static java.lang.String.*;
import static java.lang.System.*;
import static java.util.Collections.*;
import static java.util.concurrent.TimeUnit.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.*;

public interface MultiFieldDiscardSafetyTest
{
	long TEST_DURATION_MINUTES = 1;
	int CONSUMERS = Runtime.getRuntime().availableProcessors() * 7;

	static void main(final String[] args)
	{
		final long stopTime = currentTimeMillis() + MINUTES.toMillis(TEST_DURATION_MINUTES);

		final CoordinatesProducer producer = new CoordinatesProducer();
		final List<CoordinatesConsumer> consumers =
			generate(CoordinatesConsumer::new).limit(CONSUMERS).collect(toList());

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

	@RecycleBinSize(4) @SingleProducer final @Value class Coordinates implements Recyclable
	{
		private double _lat, _lon;

		private Coordinates(final double lat, final double lon)
		{
			_lat = lat;
			_lon = lon;
		}

		static Coordinates at(final double lat, final double lon)
		{
			return RecycleBin.forClass(Coordinates.class).recycle(
				discarded ->
				{
					discarded._lat = lat;
					discarded._lon = lon;
				},
				() -> new Coordinates(lat, lon)
			);
		}

		@Override public boolean isDiscarded()
		{
			return isNaN(_lat) && isNaN(_lon);
		}

		@Override public void discard()
		{
			_lat = NaN;
			_lon = NaN;
		}

		public double getLatitude()
		{
			Assert.not(this::isDiscarded);
			return _lat;
		}

		public double getLongitude()
		{
			Assert.not(this::isDiscarded);
			return _lon;
		}
	}

	final class CoordinatesProducer implements Supplier<Coordinates>
	{
		private final Random _random = new Random();
		private double _currentLat, _currentLon;
		private double _total;
		private long _count;

		public void update()
		{
			_count++;
			_currentLat = _random.nextDouble();
			_currentLon = _random.nextDouble();
			_total += _currentLat + _currentLon;
		}

		@Override public Coordinates get() { return Coordinates.at(_currentLat, _currentLon); }

		@Override public String toString()
		{
			final RecycleBin<Coordinates> recycleBin = RecycleBin.forClass(Coordinates.class);
			return format("producer: (%d) %s -- %s", _count, new BigDecimal(_total), recycleBin);
		}

		public double finish()
		{
			out.println(this);
			return _total;
		}
	}

	final class CoordinatesConsumer implements Consumer<Coordinates>
	{
		private double _total;
		private long _count;

		private final ExecutorService _executor = Executors.newSingleThreadExecutor();

		@Override public void accept(final Coordinates qty)
		{
			_executor.submit(() ->
			{
				_count++;
				final double subtotal = qty.getLatitude() + qty.getLongitude(); // DO NOT inline!
				qty.discard();
				_total += subtotal;
			});
		}

		/** @return {@code true} if the consumer's total matches the producer's; {@code false} if there's a diff. */
		public boolean finish(final double producerTotal)
		{
			final Future<Boolean> match = _executor.submit(() ->
			{
				final boolean totalsMatch = _total == producerTotal;
				out.println(totalsMatch ? this : this + " <-- DIFF!");
				return totalsMatch;
			});
			_executor.shutdown();
			try
			{
				if (!_executor.awaitTermination(10, SECONDS)) { err.println("Timed out waiting for termination"); }
				return match.get();
			}
			catch (final InterruptedException e)
			{
				err.println("Unable to shut down a consumer!");
				Thread.currentThread().interrupt();
			}
			catch (final ExecutionException e) { err.println("Unable to check the result: " + e); }
			return false;
		}

		@Override public String toString() { return format("consumer: (%d) %s", _count, new BigDecimal(_total)); }
	}
}
