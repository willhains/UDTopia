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

## Thread Safety

It's always safe to call `discard` from any thread.

By default, `recycle` is also safe to call from any thread.
When **exactly one** thread will call `recycle` on a UDT class, consider adding the `@SingleProducer` annotation.
This will remove thread safety protection from the `recycle` method, improving performance slightly.
<!-- TODO: Link to benchmark comparing with & without @SingleProducer. -->

| API       | Default Behaviour | With `@SingleProducer`                             |
|-----------|-------------------|----------------------------------------------------|
| `discard` | thread-safe       | thread-safe                                        |
| `recycle` | thread-safe       | :warning: **not** thread-safe, but slightly faster |

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
