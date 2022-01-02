# Testing Policy

UDTopia aims to be a foundational element of your app.
To earn that trust, UDTopia must have exceptional quality and reliability.
High-quality testing leads to high-quality software.

Below are the details of these goals, and guidelines on how to achieve them.

## Unit Tests

Use [JUnit 4][junit] for unit tests.
Use [Mockito][mockito] for stubbing/mocking.

[junit]: https://junit.org/junit4/
[mockito]: https://site.mockito.org

## Test Coverage

Use [JaCoCo][jacoco] to measure code coverage.
Branch coverage should be **100%**.

[jacoco]: https://www.jacoco.org/jacoco/

## Assertions

Use assertions to check method usage and parameters.
Assertions should be **enabled in Dev and Test environments**, and **disabled in Staging and Production**.

### Use `Assert` Instead of `assert`

JaCoCo [has trouble with `assert` statements][asserts].
It always reports missed branches, whether assertions are enabled or not.
Instead of invoking `assert` directly, use UDTopia’s `Assert` class, like this:

[asserts]: https://github.com/jacoco/jacoco/wiki/filtering-JAVAC.ASSERT

| Using `assert`                        | Using `Assert`                              |
|---------------------------------------|---------------------------------------------|
| `assert str.startsWith(“A”)`          | `Assert.that(() -> str.startsWith(“A”))`    |
| `assert !str.isEmpty()`               | `Assert.not(str::isEmpty)`                  |
| `assert str != null`                  | `Assert.notNull(() -> str)`                 |
| `assert str != null : “str is null!”` | `Assert.notNull(() -> str, “str is null!”)` |

More details [in the docs](docs/Assert-and-AssertControl.md).

## Test Strength

Use [PIT][pit] mutation testing to measure test strength.
UDTopia test strength should be **100%**, with the `STRONGER` mutator group.

[pit]: https://pitest.org

## Micro-Benchmarks

Use [JMH][jmh] to measure performance.
To make benchmarking easier, `BaseBenchmark` takes care of the boilerplate code.

[jmh]: https://github.com/openjdk/jmh

```java
public class StringBenchmark extends BaseBenchmark
{
  public static void main(String[] args) { runBenchmark(args); }
  @Benchmark public boolean stringEmpty() { return RAND_STR.get().isEmpty(); }
  @Benchmark public boolean stringPrefix() { return RAND_STR.get().startsWith(“A”); }
  @Benchmark public boolean numberEven() { return RAND.nextInt() % 2 == 0; }
}
```

`BaseBenchmark` provides convenient APIs for generating random content in benchmark tests.

More details [in the docs](docs/BaseBenchmark.md).
