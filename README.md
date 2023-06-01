[![Zero Runtime Dependencies][badge-dependencies]][dependencies]
[![Beta Build Status][badge-build-beta]][build-beta]
[![JaCoCo Coverage][badge-coverage]][coverage]
[![PIT Test Strength][badge-test-strength]][test-strength]
[![Javadoc][badge-javadoc]][javadoc]
[![Latest Release][badge-release]][releases]

[badge-dependencies]: https://shields.io/badge/dependencies-none-informational "Zero Runtime Dependencies"
[dependencies]: #how-to-use

[badge-build-beta]: https://shields.io/github/actions/workflow/status/willhains/udtopia/build-beta.yml?label=beta+build
[build-beta]: https://github.com/willhains/UDTopia/actions/workflows/build-beta.yml "Beta Build Status"

[badge-coverage]: https://shields.io/badge/dynamic/xml?url=https://gist.githubusercontent.com/willhains/2ce85915e469a4357c87467a748ae665/raw/jacoco.xml&label=coverage&query=round%28%2Freport%2Fcounter%5B%40type%3D%22COMPLEXITY%22%5D%2F%40covered%2A100%20div%20sum%28%2Freport%2Fcounter%5B%40type%3D%22COMPLEXITY%22%5D%2F%40%2A%5Bname%28%29%3D%27covered%27%20or%20name%28%29%3D%27missed%27%5D%29%29&suffix=% "JUnit Test Coverage by JaCoCo"
[coverage]: TESTING.md#test-coverage

[badge-test-strength]: https://shields.io/badge/dynamic/xml?url=https://gist.githubusercontent.com/willhains/403cd889045c89c1026d40e6b635d421/raw/mutations.xml&label=test+strength&query=count%28%2F%2Fmutation%5B%40status%3D%22KILLED%22%5D%29%2A100%20div%20count%28%2F%2Fmutation%29&suffix=% "Test Strength by PIT Mutation Testing"
[test-strength]: TESTING.md#test-strength

[badge-javadoc]: https://javadoc.io/badge2/org.udtopia/udtopia/javadoc.svg
[javadoc]: https://javadoc.io/doc/org.udtopia/udtopia "Javadoc API Documentation"

[badge-release]: https://shields.io/github/v/release/willhains/udtopia?display_name=tag&include_prereleases
[releases]: https://github.com/willhains/udtopia/releases "UDTopia Release History"

# UDTopia

User-defined types (UDTs) are the best way to control complexity and bring clarity to your code, but UDTs are unwieldy in Java.
UDTopia makes Java UDTs delightful.

## How to Use

UDTopia has no runtime dependencies.
You can [download][releases] and use the `.jar` file directly, or [add `org.udtopia:udtopia` as a dependency][dependency] in your project.

[releases]: https://github.com/willhains/udtopia/releases
[dependency]: https://search.maven.org/artifact/org.udtopia/udtopia

```xml
<dependency>
  <groupId>org.udtopia</groupId>
  <artifactId>udtopia</artifactId>
  <version>RELEASE <!-- sub for real version --></version>
</dependency>
```

## Features

UDTopia makes it trivial to wrap a raw value in a custom class (UDT).
For example, instead of using `String` to represent user IDs, wrap them in a `UserId` UDT:

```java
public final @Value class UserId extends PureString<UserId>
{
  public UserId(String id) { super(UserId::new, id); }
}
```

Why do this?

- A well-named UDT makes code clearer and less error-prone.
- We can add methods to `UserId` for logic related to user IDs.
- UDTopia's `Pure*` classes take care of the usual Java class boilerplate, like `equals`, `hashCode`, and `Comparable`.
  More info in the [docs][pure].

[pure]: docs/Pure-Value.md

### Validate and Normalize Values

We don't allow just *any* old string as a user ID; there are **rules**.
Let's constrain `UserId` so that invalid user IDs *can't exist*.

UDTopia's [Rule Annotations][rules] make it easy:

[rules]: docs/Constrain-Values.md

```java
@Trim // trim whitespace from start & end
@Chars(LETTERS + DIGITS + "_") // allowed chars
@Min(2) @Max(18) // allowed length
@LowerCase // convert to lowercase
public final @Value class UserId extends PureString<UserId>
```

UDTopia comes with several [built-in rules][rule-list], and you can [create your own][custom-rules].

[rule-list]: docs/Constrain-Values.md#built-in-rules
[custom-rules]: docs/Constrain-Values.md#custom-rules

### Recycle UDT Objects

GC pressure is rarely a problem with UDT objects.
But, for when profiling reveals excessive GC activity, UDTopia has an advanced [object recycling][recycle] feature.

[recycle]: docs/Recycle-Bin.md

```java
UserId userId = UserId.parse(input);
// process the value...
userId.discard();
```

Recycling is [fast][recycle-perf] and [thread-safe][recycle-threads].
It's built into UDTopia's `Recyclable*` base classes, and you can [use it in your own classes][custom-recycle], too.

[recycle-perf]: https://jmh.morethan.io/?gist=a1c976a7a3fedd8f0314ed295f5209a0#org.udtopia.recycle.JavaAllocBenchmark
[recycle-threads]: docs/Recycle-Bin.md#thread-safety
[custom-recycle]: docs/Recycle-Bin.md#advanced-use-the-recycle-bin-directly-in-a-custom-class

## :wave: Get in Touch

Have questions?
Need help?
Check the [FAQ](FAQ.md), search the [issue tracker][issues], or send a toot to [@udtopia@jvm.social][mastodon-udtopia].

Want to help?
Found a bug?
Have an idea?
Check out the [contribution guidelines](CONTRIBUTING.md) to get started.

[![Issues on GitHub][badge-issues]][issues]
[![UDTopia on Mastodon][badge-mastodon-udtopia]][mastodon-udtopia]
[![Will Hains on Mastodon][badge-mastodon-willhains]][mastodon-willhains]
[![Will Hains on GitHub][badge-github-willhains]][github-willhains]

[badge-issues]: https://img.shields.io/github/issues/willhains/udtopia
[issues]: https://github.com/willhains/UDTopia/issues

[badge-mastodon-udtopia]: https://img.shields.io/mastodon/follow/110468071106865312?domain=https%3A%2F%2Fjvm.social&label=%40udtopia&logo=mastodon&style=flat
[mastodon-udtopia]: https://jvm.social/@udtopia "UDTopia on Mastodon"

[badge-mastodon-willhains]: https://img.shields.io/mastodon/follow/109678522881106287?domain=https%3A%2F%2Fmastodon.social&label=%40willhains&logo=mastodon&style=flat
[mastodon-willhains]: https://mastodon.social/@willhains "Will Hains on Mastodon"

[badge-github-willhains]: https://shields.io/github/followers/willhains?style=flat&logo=github&label=willhains
[github-willhains]: https://github.com/willhains "Will Hains on GitHub"
