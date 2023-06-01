# Use `BaseBenchmark` for JMH Tests

To make [JMH][jmh] benchmarking easier, `BaseBenchmark` takes care of the boilerplate code.
It doesn't have any dependencies other than JMH, so you can use it in your own apps, too.

[jmh]: https://github.com/openjdk/jmh

It's easy:

1. Subclass `BaseBenchmark`.
   Append the `…Benchmark` suffix to the subclass name.
2. Add a `main(args)` method that calls `runBenchmark(args)`.
3. Write `@Benchmark` methods.

```java
public class StringBenchmark extends BaseBenchmark
{
  public static void main(String[] args) { runBenchmark(args); }
  @Benchmark public boolean stringEmpty() { return RAND_STR.get().isEmpty(); }
  @Benchmark public boolean stringPrefix() { return RAND_STR.get().startsWith("A"); }
  @Benchmark public boolean numberEven() { return RAND.nextInt() % 2 == 0; }
}
```

## Random Values

`BaseBenchmark` provides convenient APIs for generating random content in benchmark tests:

- `Random RAND` for random numbers.
- `Supplier<String> RAND_STR` for random 16-character `String`s.
  The random strings contain all the [printable ASCII characters][printable], with equal probability.

[printable]: https://en.wikipedia.org/wiki/ASCII#Printable_characters

## Benchmark Results

When run, `BaseBenchmark` prompts for an optional test name.
The output file name includes the test name, so it's easy to find.
If you forget to enter a test name, `BaseBenchmark` will time out after 30s and proceed with no test name.

The JMH output files are in JSON format, saved under the `results/benchmarks` directory.
[Johannes Zillman][jzillmann] made a powerful way to analyse these files: the brilliant [JMH Visualizer][jmh-visual].

[jzillmann]: https://github.com/jzillmann
[jmh-visual]: https://jmh.morethan.io

## Measurements

`BaseBenchmark` runs JMH in Throughput mode, measuring operations per millisecond.
It uses JMH parameter defaults tuned to give reasonably consistent results in about 1.5 minutes per `@Benchmark` method.
You can customize any of the JMH options by adding annotations to your `*Benchmark` subclass.
