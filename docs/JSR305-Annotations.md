# JSR305 Annotations

Null pointers are a scourge.
[JSR305][jsr305] proposed annotations to help dev tools track possible null values.
UDTopia uses `@Nullable`, `@Nonnull`, and `@NonNullByDefault` strictly to annotate all fields, method parameters and return types, local variables, and generic parameters.

[jsr305]: https://jcp.org/en/jsr/detail?id=305

UDTopia reimplements `@Nullable` to allow `TYPE_USE`, which means `@Nullable` should always appear immediately to the left of the type.
Think of `@Nullable String` as a *supertype* of `@Nonnull String`.
