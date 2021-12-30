# Pure Value Classes

UDTopia's `Pure*` base classes make it easy to wrap basic values in rich, well-named UDTs.

| To Wrap This                            | Extend This  |
|-----------------------------------------|--------------|
| primitive `double`                      | `PureDouble` |
| primitive `long`                        | `PureLong`   |
| primitive `int`                         | `PureInt`    |
| `String`[^not-null]                     | `PureString` |
| any other object[^not-null][^not-array] | `PureValue`  |

[^not-null]: `null` is not supported.

[^not-array]: `PureValue` doesn't support array objects.
  Arrays need special handling for `equals`, `hashCode`, and `toString`, which would be slower.
  Typically, `Pure*` classes are for *single* values, but if you really need a collection, use a `List` or `Set`.

## How to Wrap a Value

```java
public final @Value class BodyTemp extends PureDouble<BodyTemp>
{
  public BodyTemp(double reading) { super(BodyTemp::new, reading); }
}
```

1. Declare a class and extend `PureDouble`, `PureInt`, `PureLong`, `PureString`, or `PureValue`.
   Repeat your class name in the generic type.
2. Make the class `final`.
   You can also add the `@Value` annotation to document that it's a pure value.
   (Read more about that [here][roles].)
3. Declare a constructor that takes a single argument (the raw value); and passes a method reference to itself, and the raw value, to the superclass constructor.

[roles]: Role-Annotations.md

If you prefer to [expose static factory methods instead of the constructor][effective-java-1], declare it like this:

```java
public final @Value class BodyTemp extends PureDouble<BodyTemp>
{
  private BodyTemp(double reading) { super(BodyTemp::inCelsius, reading); }
  public static BodyTemp inCelsius(double reading) { return new BodyTemp(reading); }
  public static BodyTemp inFahrenheit(double reading) { return inCelsius((reading - 32.0) / 1.8); }
}
```

The constructor/factory method reference allows methods like `map` to return new instances of your subclass.
More about that [below](#mapping-values).

[effective-java-1]: http://index-of.es/Java/Effective%20Java.pdf#page=28

### Pure Values are Immutable

Other than `PureValue`, the underlying raw type (primitive or `String`) guarantees immutability.

`PureValue` can wrap any raw type, so when that raw type is mutable, we need to do a little more to ensure immutability.
We need to provide a way to make a [defensive copy][defensive].

[defensive]: http://www.javapractices.com/topic/TopicAction.do?Id=15

```java
public final @Value class MousePosition extends PureValue<Point, MousePosition>
{
  public MousePosition(Point point)
  {
    super(MousePosition::new, point, p -> new Point(p.x, p.y));
  }
}
```

`PureValue` will automatically make a defensive copy in the constructor, and when passing or returning the value to other objects.

## Why Wrap Values?

### 1. Naming Things

A string is always a `String`, but we can name UDTs whatever we want.
Not only the names of UDT classes, but also their methods and parameters.
Well-chosen names make code clearer, easier to read, and self-documenting.

### 2. Purpose and Constraints

Applications deal with a lot of raw values — mostly `String`, but also some `int` and `long`.
But a `String` is essentially an unbounded, unconstrained array of bytes.
Do we really allow *any* string of characters in *every* place we use `String`?
Similarly, do we really allow *any* integer value in *every* place we use `int` or `long`?

Of course not.
Each is constrained to a set of valid values, with a specific purpose.

Clearly, our `BodyTemp` class should not allow the full range of `double` values.
We should trap values outside the valid range.

```java
public BodyTemp(double reading)
{
  super(BodyTemp::new, reading);
  if (reading < MIN_BODY_TEMP || reading > MAX_BODY_TEMP)
  {
    throw new IllegalArgumentException("Invalid body temp: " + reading);
  }
}
```

Raw values enter our app from the outside world: user input, files, databases, network services, and so on.
The best place to trap invalid raw data is at the entry point, the "edges" of our app.
In this way, we can trust that all values we process internally are valid.
That means we don't need extra logic to guard against invalid data in our core logic.
Clean data means cleaner, simpler code.

### 3. Prevent Accidental Substitution

Since each value has a specific purpose, we have to be careful to not get them mixed up.
Substitute a user's first name with their email address, and we'd have an embarrassing bug.
Substitute a stock trade's price with its quantity, and we could go out of business!

It's a shame the compiler can't help us catch these bugs before we launch the app…
Or can it?

When we wrap values in UDTs, we give them *names*, so they never get mixed up.
Even better, the compiler won't let us accidentally substitute one for another.

```java
public final @Value class BloodOxygen extends PureDouble<BloodOxygen>
{
  public BloodOxygen(double reading)
  {
    super(BloodOxygen::new, reading);
    if (reading < MIN_BLOOD_O2 || reading > MAX_BLOOD_O2)
    {
      throw new IllegalArgumentException("Invalid blood oxygen: " + reading);
    }
  }
}
```

Without UDTs, our app would have to take two `double` inputs.
There might be a method like `recordVitals(double, double)`.
How will developers calling this method know which `double` is which?
Well, they could look at the Javadoc, or read the source code to find the parameter names.

But, with UDTs, the method would be `recordVitals(BodyTemp, BloodOxygen)`.
It is *impossible* to get them mixed up; the compiler won't let us.
Deeper inside the app, where we process these values, not only is it easy to identify and distinguish them, but we can also be confident they are valid values.

### 4. Move Logic Closer to Data

By creating a class to wrap the data, we also create a place to put logic related to that data: as methods of the class.

```java
public boolean isFever() { return getAsDouble() > FEVER_THRESHOLD; }
public double getAsFahrenheit() { return getAsDouble() * 1.8 + 32.0; }
```

When we keep the logic close to the data it processes, it's easier to reason about, easier to test, and more reusable.

### But What About Performance?

The JVM is excellent at optimizing and inlining at runtime.
[Benchmarks show][RawVsUDTBenchmark] there's not much difference in throughput or latency performance between a raw value and a UDT.

[RawVsUDTBenchmark]: https://jmh.morethan.io/?gist=dc1e22d2ed81ddceff20346455c7c814

### But What About Garbage Collection?

Particularly when compared to primitive types, which don't allocate objects on the heap, UDTs do generate more GC pressure.
However, the GC algorithms of modern JVMs are extraordinarily sophisticated.
Java GC is not the performance drag it once was.
Do not fear the GC Bogeyman!

## How to Use a Wrapped Value

`Pure*` classes implement [Java's supplier interfaces][supplier].
To get the raw value, call one of the `get*` methods.

[supplier]: https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html#package.description

| Pure Class       | Supplier Interface | Getter Method   | Type Conversion                       |
|------------------|--------------------|-----------------|---------------------------------------|
| `PureDouble`     | `DoubleSupplier`   | `getAsDouble()` |                                       |
|                  | `LongSupplier`     | `getAsLong()`   | Rounds to the closest `long` value.   |
|                  | `IntSupplier`      | `getAsInt()`    | Rounds to the closest `int` value.    |
| `PureLong`       | `DoubleSupplier`   | `getAsDouble()` | Primitive cast (widening conversion). |
|                  | `LongSupplier`     | `getAsLong()`   |                                       |
|                  | `IntSupplier`      | `getAsInt()`    | Rounds to the closest `int` value.    |
| `PureInt`        | `DoubleSupplier`   | `getAsDouble()` | Primitive cast (widening conversion). |
|                  | `LongSupplier`     | `getAsLong()`   | Primitive cast (widening conversion). |
|                  | `IntSupplier`      | `getAsInt()`    |                                       |
| `PureString`     | `Supplier<String>` | `get()`         |                                       |
| `PureValue<Raw>` | `Supplier<Raw>`    | `get()`         |                                       |

### `hashCode`, `equals`, and `toString`

When extending a `Pure*` class, don't add any independent fields.
They are meant to be single values, used as fields in other classes, elements of collections, and in method signatures.
They include correct implementations of `hashCode()` and `equals(@Nullable Object)`, which are final.
The `toString()` implementations are not final, and just return the default string conversion of the raw value by default.
You can override `toString()` to customize its format.

To supplement `equals(@Nullable Object)`, there's a shortcut `eq(This)` method.
It accepts only non-null objects of the same type, and [is faster][EqVsEqualsBenchmark] than `equals`.

[EqVsEqualsBenchmark]: https://jmh.morethan.io/?gist=a20c25710402e117fb62146128017ea5

## Comparing Values

The following `Pure*` classes are [`Comparable`][Comparable] via the `UDTComparable` interface.
We can use `compareTo(This)` to compare with other values of the same class.

[Comparable]: https://docs.oracle.com/javase/8/docs/api/java/lang/Comparable.html

| Pure Class   | Comparable                         |
|--------------|------------------------------------|
| `PureDouble` | yes                                |
| `PureLong`   | yes                                |
| `PureInt`    | yes                                |
| `PureString` | yes                                |
| `PureValue`  | yes, if `implements UDTComparable` |

`UDTComparable` classes expose more useful methods:

- `min(This)`  
  `max(This)`  
  Return the lesser or greater of two values.
  For example, we can easily find the maximum value in a stream, using: `stream.reduce(MyUDT::max)`

- `isGreaterThan(This)`  
  `isLessThan(This)`  
  `isGreaterThanOrEqualTo(This)`  
  `isLessThanOrEqualTo(This)`  
  Compare two values.

## Mapping Values

We can operate on the raw value, producing new UDT values, without unwrapping and re-wrapping them.[^new-instance]

[^new-instance]: Note: Since pure values are immutable, each `map` produces a new instance with the mapped value.

```java
final ArticleTitle title = new ArticleTitle(formInput.get("title"));
final ArticleTitle trimmed = title.map(String::trim);
final ArticleTitle capitalized = trimmed.map(WordUtils::capitalizeFully);
final ArticleTitle noDot = capitalized.map(t -> t.replaceAll("\\.$", ""));
```

Or, to map the raw value and convert to another UDT, just add a constructor reference for the destination type.

```java
final UrlSlug urlSlug = noDot.map(t -> t.replaceAll("\\s", "-"), UrlSlug::new);
```

## Checking Values

We can check the raw value without unwrapping it, using the `is` and `isNot` methods.

```java
if (title.isNot(String::isEmpty))
{
  if (title.is(t -> t.length() > LONG_TITLE_LENGTH))) {...}
}
```

## Numeric and Arithmetic Operations

The numeric `Pure*` classes (`PureDouble`, `PureLong`, `PureInt`) provide some useful numeric operations:

- `add`  
  *(multiple signatures)*  
  Addition with overflow protection.

- `subtract`  
  `subtractFrom`  
  *(multiple signatures)*  
  Subtraction with overflow protection.

- `multiplyBy`  
  *(multiple signatures)*  
  Multiplication with overflow protection.

- `divideBy`  
  `divide`  
  *(multiple signatures)*  
  Division with rounding.

- `isZero()`  
  `isNonZero()`  
  Check if the raw value is zero.

- `isPositive()`  
  `isNegative()`  
  Check the sign of non-zero values.

- `negate()`  
  Flip the sign of non-zero values.

- `invert()`  
  Get the inverse of non-zero values.

- `format(NumberFormat formatter)`  
  `format(String pattern)`  
  Format the number as a string.

Numeric operations work with primitive values, and UDTs of different classes can work together:

```java
// This is only an illustration
// Don't use floating-point values for money!
final Quantity orderQuantity = new Quantity(5);
final Price unitPrice = new Price(24.95);
final Price orderTotal = unitPrice.multiplyBy(orderQuantity);
final Price withTax = orderTotal.multiplyBy(1.15);
```
