# UDTopia Documentation Overview

Documentation for UDTopia can be found in:

- The **[README](README.md)**, a high-level overview of UDTopia, with some simple usage examples.
- The **[docs](docs/)**, more in-depth explanation of features and how to use them.
- The **[Javadocs][javadoc]**, formal API specifications.
- Policy statements (all-caps `.md` files in the repo root, like this one) define project policies.

[readme]: README.md
[docs]: docs/
[javadoc]: https://javadoc.io/doc/org.udtopia/udtopia/latest/index.html

Documentation is hard.
If you notice something missing or wrong, please create an issue, and/or submit a PR to fix it!

## General Documentation Guidelines

- All documentation and code comments should be written in [Plain English][plain].
  Strive for clarity.
  [Omit needless words][omit].
  Apart from being easier to read by more people, it also helps machine translation to other languages.

- Use [Oxford spelling][oxford].

[plain]: http://www.plainenglish.co.uk/free-guides.html
[oxford]: https://en.wikipedia.org/wiki/Oxford_spelling
[omit]: https://www.bartleby.com/141/strunk5.html#13

## Markdown Guidelines

See the [code style guidelines doc][style] for a list of Markdown conventions.

[style]: CODE_STYLE.md

## Javadoc Guidelines

- All `public` and `protected` members should have Javadoc comments.

- Avoid the `@author` tag, except if the code author and committer are different people.
  (Example: code copied from another project.)

- The audience for Javadoc is developers.
  As such, it should be formal and precise.
  Be sure to document edge-case behaviour.
