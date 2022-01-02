# Coding Style Guide

UDTopia adheres strictly to the code style conventions described below.
These might not be what you're used to.

## Automation

Code formatting is a tedious and error-prone job, best left to automation.
To minimize human effort, the repo includes:

- Config for [code style][style] and [inspections][inspections], for [IntelliJ IDEA][idea] users.
- An [EditorConfig file][editorconfig], for other editors and IDEs.

[idea]: https://www.jetbrains.com/idea/
[style]: .idea/codeStyles/codeStyleConfig.xml
[inspections]: .idea/inspectionProfiles/Project_Default.xml
[editorconfig]: .editorconfig

## Java Code Style

This section doesn't yet have a full definition of UDTopia's Java coding conventions.
In the meantime, here are some key points:

- **Use [Allman-style][allman] braces.**
  Open braces (`{`) go on their own line, unless on the same line as the closing brace (`}`).
  Open and close brace pairs are either in the same row, or in the same column.

- **Indent with tabs, not spaces.**
  Assume the tab width is four, but avoid formatting that depends on this assumption.
  Changing the editor's tab width should not break readability.

- **Private members begin with an underscore.**
  This includes constants, fields, methods, and nested types.

[allman]: https://en.wikipedia.org/wiki/Indentation_style#Allman_style

## Markdown Code Style

- Indent with two spaces.

- Write one sentence per line.
  Do not wrap lines in the middle of a sentence.
  There's no line length limit, other than the general documentation guideline to keep sentences short.

- Use hyphens (`-`) for bullet lists.

- Use hashes (`#`) for heading markers.

- Prefer [reference-style][links] links over [inline-style](https://daringfireball.net/projects/markdown/syntax#link).

- Use named footnotes.

[links]: https://daringfireball.net/projects/markdown/syntax#link
