# Constrain Values with Rule Annotations

UDTopia provides a convenient, declarative way to constrain values, with **rule annotations**.
Rules can **normalize** values to conform to constraints, or **validate** and reject invalid values.

```java
@Min(MIN_BODY_TEMP) @Max(MAX_BODY_TEMP)
public final @Value class BodyTemp extends PureDouble<BodyTemp>
{
  public BodyTemp(double reading) { super(BodyTemp::new, reading); }
}

@Trim @LowerCase @Max(40) @Matching("[a-z0-9-]+")
public final @Value class UrlSlug extends PureString<UrlSlug>
{
  public UrlSlug(String slug) { super(UrlSlug::new, slug); }
}
```

## What is This Good For?

Rules are for constraining values.
Think of it as the UDT's *range*.
The goal is to [make invalid values *unrepresentable*][parse-don't-validate].

[parse-don't-validate]: https://lexi-lambda.github.io/blog/2019/11/05/parse-don-t-validate/

When it's possible, reasonable, and unsurprising to automatically convert a wrong value to a correct one, use a **normalization** rule.

When it's impossible (or unreasonable, or surprising) to normalize, use a rule to **reject** invalid values.
Rules are not for business logic; they're for making guarantees about what a UDT value can and can't be.
They push the concern of invalid data to the edges of the app, where it has to interact with the messy outside world.

## Built-In Rules

| Rule Annotation | What it Does                                                                   |
|-----------------|--------------------------------------------------------------------------------|
| `@Floor`        | Normalize numeric values to a minimum value.                                   |
| `@Ceiling`      | Normalize numeric values to a maximum value.                                   |
| `@Round`        | Normalize numeric values by rounding to an increment value.                    |
| `@LowerCase`    | Normalize string values to lower case.                                         |
| `@UpperCase`    | Normalize string values to upper case.                                         |
| `@Replace`      | Normalize string values by replacing substrings matching a regular expression. |
| `@Trim`         | Normalize string values by trimming whitespace from the beginning and end.     |
| `@Min`          | Validate numeric values against a minimum allowed value.                       |
|                 | Validate string values against a minimum allowed length.                       |
| `@Max`          | Validate numeric values against a maximum allowed value.                       |
|                 | Validate string values against a maximum allowed length.                       |
| `@GreaterThan`  | Validate numeric values against an exclusive lower bound.                      |
| `@LessThan`     | Validate numeric values against an exclusive upper bound.                      |
| `@MultipleOf`   | Validate integer values against a divisible factor (increment value).          |
| `@Chars`        | Validate string values against a set of allowed characters.                    |
| `@NotChars`     | Validate string values against a set of disallowed characters.                 |
| `@Matching`     | Validate string values against an allowed regular expression.                  |
| `@NotMatching`  | Validate string values against a disallowed regular expression.                |

If you have an idea for a rule annotation that would be generally useful, [let's talk](../CONTRIBUTING.md)!

## Skip Some Rules Conditionally

Some rule annotations include a `when` parameter, to apply them conditionally.

```java
@Min(value = MIN_BODY_TEMP, when = ASSERTS_ENABLED)
```

- **`ALWAYS`** (the default) always applies the rule.
- **`ASSERTS_ENABLED`** applies the rule only if assertions are enabled in the JVM.[^per-class-assertions]
  This is useful for validating more heavily in Dev and Test environments.

[^per-class-assertions]: Java allows [control of assertions][ea] at the JVM, classloader, package, and class levels.
  UDTopia will check the assertion status of the annotated class.

[ea]: https://docs.oracle.com/cd/E19683-01/806-7930/6jgp65ikq/index.html
