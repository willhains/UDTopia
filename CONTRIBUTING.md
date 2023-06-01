# Contributing to UDTopia

First, **thank you** for even considering donating your time and skills to help UDTopia!
Here's to you!
:beers:

UDTopia is young, and still finding its feet.
The goals and vision for UDTopia mostly exist only in [my head][willhains], and I haven't documented them properly.
I'm working on that; but in the meantime, I need [all the help I can get][help-wanted] finding and squashing bugs, writing tests and documentation, and improving performance.

[willhains]: https://github.com/willhains
[help-wanted]: https://github.com/willhains/UDTopia/labels/help%20wanted

Below is a set of gentle guidelines for contributing to UDTopia.
These are merely suggestions, not rules.
Please use common sense and judgement, and feel free to propose changes to this document in a pull request.

## Ask Questions, Give Feedback, or Just Say "Hi!" :wave:

First, check whether the [FAQ](FAQ.md) already has an answer to your question.
If not, the [issue tracker][issues] is the best place to ask questions, even if not reporting a problem.
Use the [question label][questions] for that.
In fact, most communication about the project is best done through issues.[^not-issues]

[issues]: https://github.com/willhains/UDTopia/issues
[questions]: https://github.com/willhains/UDTopia/labels/question
[^not-issues]: One notable exception where issues are *not* appropriate: abuse reports. 
  Read the [code of conduct](CODE_OF_CONDUCT.md) for how to escalate.

For informal conversations, send a toot or DM to [@udtopia@jvm.social][mastodon].

[mastodon]: https://jvm.social/@udtopia

All discourse, in the issue tracker or elsewhere, is subject to the [code of conduct](CODE_OF_CONDUCT.md).

## Write Helpful Bug Reports

Try to include these elements:

- How to reproduce the problem
- What is the expected behaviour
- What do you actually see

If you can provide code that reproduces the problem, great!
If this code is in the form of a failing unit test, even better!

## Issue or Pull Request?

As a rule of thumb, it's usually best to raise an issue first, before providing a pull request.

It's a good idea to first check we agree there actually is a problem to solve.
For complex pull requests, it may save time to discuss in advance what shape the solution should take.

That said, for bugfixes and documentation fixes, reporting the issue and providing a fix in one PR is perfectly fine.
When in doubt, maybe raise an issue first.

If there's a corresponding GitHub issue, please [mention the issue number in the pull request][PR-link] title.
Ideally, [prefix commit messages][prefix] with either the pull request number, or the associated issue number.

[PR-link]: https://docs.github.com/en/issues/tracking-your-work-with-issues/linking-a-pull-request-to-an-issue
[prefix]: https://docs.github.com/en/github/writing-on-github/working-with-advanced-formatting/autolinked-references-and-urls

## Adhere to Project Conventions

When submitting a PR, please adhere to the project's [documentation guidelines][documentation] and [code style][style].
Before merging, we may ask for changes to code style, documentation, etc.

[documentation]: DOCUMENTATION.md
[style]: CODE_STYLE.md

Code submissions must pass the build, which includes thresholds for code coverage, test strength, and more.

## Java Version

The project targets Java 8, to be useful to as many teams as possible.
Java 9+ language features are not available.
