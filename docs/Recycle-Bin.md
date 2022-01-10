# Recycle Bin

`RecycleBin` is an advanced feature to minimise GC pressure, by reusing UDT instances.
Use it when profiling shows your app is suffering from excessive GC activity.

## How to Convert a `Pure*` UDT to `Recyclable*`

Let's upgrade our [`BodyTemp`][BodyTemp] UDT class to enable recycling.

1. Instead of extending a `Pure*` base class, extend the equivalent `Recyclable*` base class.
   ```diff
   - public final @Value class BodyTemp extends PureDouble<BodyTemp>
   + public final @Value class BodyTemp extends RecyclableDouble<BodyTemp>
   ```

2. Make the constructor private, and add a public static factory method.
   ```diff
   - public BodyTemp(double reading) { super(BodyTemp::new, reading); }
   + private BodyTemp(double reading) { super(BodyTemp::new, reading); }
   + public static BodyTemp inCelsius(double reading)
   + {
   +   return new BodyTemp(reading);
   + }
   ```

3. In the constructor, change the constructor reference to a factory method reference.
   ```diff
   - private BodyTemp(double reading) { super(BodyTemp::new, reading); }
   + private BodyTemp(double reading) { super(BodyTemp::inCelsius, reading); }
   ```

4. In the factory method, call `recycle(class, constructor, value)`.
   ```diff
   -   return new BodyTemp(reading);
   +   return recycle(BodyTemp.class, BodyTemp::new, reading);
   ```

[BodyTemp]: Pure-Value.md#how-to-wrap-a-value

Here is the result:

```java
public final @Value class BodyTemp extends RecyclableDouble<BodyTemp>
{
  private BodyTemp(double reading) { super(BodyTemp::inCelsius, reading); }
  public static BodyTemp inCelsius(double reading)
  {
    return recycle(BodyTemp.class, BodyTemp::new, reading);
  }
}
```

## How to Use a Recyclable UDT

```java
final BodyTemp bt = BodyTemp.inCelsius(reading);
// use the instance...
bt.discard();
```

1. Call the static factory method to get an instance.
2. Use the instance for some processing, retaining no references to the instance afterwards.
3. Call `discard()`.

The `discard()` method tells the instance it is safe to recycle.
Implicitly, we **must not access** the instance after calling `discard()`.

> :warning:
> Recycling is an **advanced feature**, which requires extra care by the developer.
> - **Do not** access the instance after calling `discard()`.
>   *Bad things will happen.*
> - It's OK to not call `discard()`.
>   The instance will just go to GC like a `Pure*`-based UDT.
> - `Recyclable*` classes reserve a special value for the discarded state:
>   `NaN` for double, `MIN_VALUE` for integers, and `null` for objects.
>   These values may not be wrapped in recyclable UDTs.
> - When assertions are enabled, UDTopia will trap incorrect `discard()` usage.
>   Enable assertions in Dev and Test environments to catch mistakes.

## Thread Safety

It's always safe to call `discard` from any thread.

By default, `recycle` is also safe to call from any thread.
When **exactly one** thread will call `recycle` on a UDT class, consider adding the `@SingleProducer` annotation.
This will remove thread safety protection from the `recycle` method, improving performance slightly.

| API       | Default Behaviour | With `@SingleProducer`                                                   |
|-----------|-------------------|--------------------------------------------------------------------------|
| `discard` | thread-safe       | thread-safe                                                              |
| `recycle` | thread-safe       | :warning: **not** thread-safe, but [slightly faster][JavaAllocBenchmark] |

[JavaAllocBenchmark]: https://jmh.morethan.io/?gist=31deb26fe4b80c5afbd24df8e9ed90f0

## Advanced: Use the Recycle Bin Directly in a Custom Class

`Recyclable*` base classes provide built-in support for the recycle bin.
You can also use the recycle bin directly to recycle instances of your own custom classes.

```java
public final @Value class Vitals implements Recyclable
{
  private @Nullable BodyTemp _bbt;
  private @Nullable BloodOxygen _saO2;

  private Vitals(final @Nonnull BodyTemp bbt, final @Nonnull BloodOxygen saO2)
  {
    Assert.notNull(() -> bbt);
    Assert.notNull(() -> saO2);
    _bbt = bbt;
    _saO2 = saO2;
  }

  public static Vitals readings(final BodyTemp bbt, final BloodOxygen saO2)
  {
    return RecycleBin.forClass(Vitals.class).recycle(
      discarded ->
      {
        discarded._bbt = bbt;
        discarded._saO2 = saO2;
      },
      () -> new Vitals(bbt, saO2));
  }

  @Override public boolean isDiscarded()
  {
    return _bbt == null && _saO2 == null;
  }

  @Override public void discard()
  {
    Assert.not(this::isDiscarded, "Attempted to discard twice!");
    _bbt = null;
    _saO2 = null;
  }

  public BodyTemp getBodyTemp()
  {
    Assert.not(this::isDiscarded, "Attempted to access discarded instance!");
    return _bbt;
  }

  public BloodOxygen getOxygenSat()
  {
    Assert.not(this::isDiscarded, "Attempted to access discarded instance!");
    return _saO2;
  }
}
```

**Key Point:** Decide a *discard value* for each field.
(In this example, we are using `null`, since the fields are object types.)
The instance is available for recycling when all the fields are their discard values.

> :memo:
> We don't need to use `volatile` for these fields, since all fields contribute to determining the discarded state.
> If we have many fields, we may prefer to use a single `volatile boolean _discarded` flag to hold the discarded state.
> Note, however, that performance may be slower with `volatile`.

## Tuning

`RecycleBin` is a leaky instance pool, by design.
It doesn't guarantee that all discarded instances will be recycled.
If an instance is discarded too late, it goes to GC like any other object, and `RecycleBin` allocates a new instance to take its place.

When assertions are enabled, each `RecycleBin` collects metrics on hits (successful recycles) and misses (new allocations).
Use `RecycleBin.forClass(MyClass.class).toString()` to see something like this:

```
RecycleBin[16]: 28,013,966 / 29,315,076 (95.6%) recycled
```

If the hit rate is too low, meaning too many instances are going to GC, try the following.

- Check whether you're calling `discard()` on all instances, right before they fall out of scope.
- Increase the size of the recycle bin, by adding `@RecycleBinSize(size)`.
  It will automatically round up to the next power of two.
  The default size is 16.
