# Recycle Bin

`RecycleBin` is an advanced feature to minimise GC pressure, by reusing UDT instances.
Use it when profiling shows your app is suffering from excessive GC activity.

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
