package org.udtopia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import static java.nio.charset.StandardCharsets.*;
import static java.util.concurrent.TimeUnit.*;
import static java.util.stream.Stream.*;

/** Base class for JMH benchmarks, with a useful set of default options. */

// Default benchmark options, inherited by subclasses
@State(Scope.Thread)
@Fork(3)
@Warmup(time = 3)
@Measurement(time = 1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(MILLISECONDS)

// Designed for extension, so that subclasses inherit the annotations above
@SuppressWarnings("UtilityClassCanBeEnum")
public abstract @Mutable class BaseBenchmark
{
	/** Random number generator. */
	protected static final Random RAND = new Random();

	// Random string generator
	private static final int _STRING_LENGTH = 16;
	private static final Supplier<String> _RAND_STRING = () -> RAND.ints(32, 127) // printable chars
		.limit(_STRING_LENGTH) // consistent string length to avoid variance between test iterations
		.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

	// Pre-generated random strings
	private static final int _STRING_COUNT = 1024;
	private static final String[] _RAND_STRINGS = generate(_RAND_STRING).limit(_STRING_COUNT).toArray(String[]::new);

	// Counter to loop around array of pre-generated random strings
	private static int _randomStringHead;

	/** Supplier of pre-generated random strings. Loops around after {@value _STRING_COUNT} values. */
	protected static final Supplier<String> RAND_STR = () -> _RAND_STRINGS[_randomStringHead++ % _STRING_COUNT];

	/** Call this method from {@code public static void main(String[] args)} in the benchmark class. */
	protected static void runBenchmark(final String[] ignored)
	{
		// Format JSON results file name
		final String benchmarkClassName = _getBenchmarkClassName();
		final String benchmarkName = benchmarkClassName.replace("Benchmark", "");
		final String testName = _promptForTestName();
		final String jsonResultsFile = _createJsonResultsPath(benchmarkName, testName);

		_executeBenchmarks(benchmarkClassName, jsonResultsFile);
	}

	private static void _executeBenchmarks(final String benchmarkPattern, final String jsonResultsFile)
	{
		// Build benchmark options
		final Options options = new OptionsBuilder()
			.include(benchmarkPattern)
			.addProfiler(StackProfiler.class)
			.addProfiler(GCProfiler.class)
			.resultFormat(ResultFormatType.JSON)
			.result(jsonResultsFile)
			.build();

		// Execute benchmark
		try { new Runner(options).run(); } catch (final RunnerException e) { e.printStackTrace(); }
	}

	/** Run all benchmarks named "*Benchmark". */
	public static void main(final String[] ignored)
	{
		final String testName = _promptForTestName();
		final String jsonResultsFile = _createJsonResultsPath(".", testName);
		_executeBenchmarks("Benchmark", jsonResultsFile);
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	private static String _createJsonResultsPath(final String benchmarkName, final String testName)
	{
		// Create parent directories if missing
		final Path resultsRoot = Paths.get("results", "benchmarks");
		final Path parentPath = resultsRoot.resolve(benchmarkName);
		parentPath.toFile().mkdirs();

		// Construct the path to the results file
		return parentPath.resolve(testName + ".json").toString();
	}

	private static String _getBenchmarkClassName()
	{
		// This custom security manager allows us to walk the stack and find the benchmark class name
		@SuppressWarnings("CustomSecurityManager") class BenchmarkClass extends SecurityManager
		{
			String fromStack()
			{
				return Arrays.stream(getClassContext())
					.filter(BaseBenchmark.class::isAssignableFrom)
					.map(Class::getSimpleName)
					.reduce((first, last) -> last)
					.orElseThrow(() -> new Error("Unable to determine benchmark name"));
			}
		}
		return new BenchmarkClass().fromStack();
	}

	@SuppressWarnings("BusyWait")
	private static String _promptForTestName()
	{
		// Prefix the test name with a timestamp, so test names can be reused and will sort chronologically
		final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm"));

		// Prompt for a test name that will make it easy to tell apart from other tests
		System.out.print("Enter test name (optional): ");
		try
		{
			// Wait up to 30s for input
			final long timeout = System.currentTimeMillis() + 30_000;
			final BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in, UTF_8));
			while (System.currentTimeMillis() < timeout && !consoleInput.ready()) { Thread.sleep(200); }

			// Use entered test name or unnamed
			final String input = consoleInput.ready() ? consoleInput.readLine() : "";
			if (input.isEmpty()) { System.err.println("No test name entered. Starting unnamed test."); }
			else { return input.trim().replaceAll("[^\\w-_.()@# ]", " "); }
		}
		catch (final IOException | InterruptedException ignored) { }

		// Return just the timestamp by default
		return timestamp;
	}
}
