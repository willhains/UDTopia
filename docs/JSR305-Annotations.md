# JSR305 Annotations

Null pointers are a scourge.
[JSR305][jsr305] proposed annotations to help dev tools track possible null values.
UDTopia uses `@Nullable`, `@Nonnull`, and `@NonNullByDefault` strictly to annotate all fields, method parameters and return types, local variables, and generic parameters.

[jsr305]: https://jcp.org/en/jsr/detail?id=305

UDTopia reimplements `@Nullable` to allow `TYPE_USE`, which means `@Nullable` should always appear immediately to the left of the type.
Think of `@Nullable String` as a *supertype* of `@Nonnull String`.

## Everything is Non-Null by Default

The Maven build includes a handy [Package Info Maven Plugin][pkginfo] by [Ryan Bohn][bohnman] to automatically generate `package-info.java` files for every package, declaring `@NonNullByDefault`.
So every un-annotated type in UDTopia is implicitly `@Nonnull`.

[pkginfo]: https://github.com/willhains/package-info-maven-plugin
[bohnman]: https://github.com/bohnman
