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
