# Annotate Class Roles

When you boil it down, all applications do four things:

1. Take input, and produce output.
2. Model and process data values.
3. Store and update that data in memory.
4. Coordinate access to that memory by threads.

In general, it helps to separate the code that performs each role, because they each have different qualities.

**Input/output** code, by its nature, is slow.
It has multiple (sometimes mysterious) modes of failure, so it tends to handle many exceptions.
It is difficult and time-consuming to test, so it's much better suited to integration testing, rather than unit testing.
As such, it's best to use as little branching logic as possible in I/O code.

**Value**-processing code, by contrast, is fast.
Typically, it has few failure modes, other than those deliberately added by the developer.
Value code is the easiest to unit test, making it the *perfect* home for an application's branching logic.

**Mutable** classes are also fast, and usually easy to unit test *in a single-threaded context*.
It is important to know where mutable state is, and how it is accessed or updated.
Practically all software bugs are related to mutable state of some kind, so it pays to keep them simple.

**Thread-control** code is fiendishly difficult — perhaps *impossible* — to unit test effectively.
The only true defence against multithreading bugs is the ability to confidently reason about it.
The best way to increase confidence is to keep it as simple as possible, and strictly separated from all other code.

UDTopia includes four annotations to document the roles of each class:

- `@IO`
- `@Value`
- `@Mutable`
- `@ThreadSafe`

These annotations don't do anything at runtime.
They're just a nice way to categorise classes by which of the above roles they perform.

Sometimes, a class may perform more than one of these roles.
Use multiple annotations to indicate this, and as a reminder to try to separate them.
