# API Stability

UDTopia aims to be a foundational element of your app.
To earn that trust, the API must be as stable as possible.

That said, sometimes it's necessary to change or remove an API, for long-term improvements.
Below explains the UDTopia policy for API changes.

## Deprecation

APIs to be removed are annotated with `@Deprecated`, and a `@deprecated` Javadoc tag.
The `@deprecated` Javadoc tag briefly explains the API to use instead.

APIs may become deprecated in a minor version (`*.x.*`).
The release notes list newly-deprecated APIs.

## Removal

Deprecated APIs may be removed in the next major version (`x.*.*`).
The release notes list newly-removed APIs.
