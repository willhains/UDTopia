# `Assert` and `AssertControl`

JaCoCo [has trouble with `assert` statements][asserts].
It always reports missed branches, whether assertions are enabled or not.
Instead of invoking `assert` directly, use UDTopia’s `Assert` class, like this:

[asserts]: https://github.com/jacoco/jacoco/wiki/filtering-JAVAC.ASSERT

| Using `assert`                                  | Using `Assert`                                             |
|-------------------------------------------------|------------------------------------------------------------|
| `assert str.startsWith("A")`                    | `Assert.that(() -> str.startsWith("A"))`                   |
| `assert str.startsWith("A") : "missing prefix"` | `Assert.that(() -> str.startsWith("A"), "missing prefix")` |
| `assert !str.isEmpty()`                         | `Assert.not(str::isEmpty)`                                 |
| `assert !str.isEmpty() : "empty string"`        | `Assert.not(str::isEmpty, "empty string")`                 |
| `assert str != null`                            | `Assert.notNull(() -> str)`                                |
| `assert str != null : "str is null!"`           | `Assert.notNull(() -> str, "str is null!")`                |

`Assert` uses Java's `assert` statement internally, but it's excluded from coverage analysis.
[Benchmarks confirm][AssertBenchmark] the performance impact of an `Assert` call is virtually zero when assertions are disabled.

[AssertBenchmark]: https://jmh.morethan.io/?gist=9b439826f9f21d2cac7976a86ecd259e

## Debug Actions

`Assert` has a `debug()` method, useful for Dev and Test environments.
It runs when assertions are enabled, but not when disabled.
Use this judiciously, since your app's behaviour will change when assertions are enabled/disabled.

```java
Assert.debug(() -> logMemoryStats());
```

## Control Assertions at Runtime

Assertions are enabled for tests by default.
To test behaviour that depends on disabled assertions, use `AssertControl`:

```java
AssertControl.DISABLE.forClass(X.class);
```

To stop the disabled assertion status leaking into other tests, add a before-after hook to the test class, like this:

```java
@Before @After public void resetAsserts() { AssertControl.ENABLE.forClass(X.class); }
```
