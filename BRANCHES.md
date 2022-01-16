# Branching Model

The UDTopia Git repo uses a three-level stability model for branches.

| Branch Name | Stability | Purpose |
|-|-|-|
| `alpha` | Least stable | Features for future releases. |
| `beta/*.*.*` | Stable, but not production-ready | Stabilize the next release version. |
| `released` | Production-ready | Released version, available on Central. |

## Pull Requests

The target branch for a pull request depends on the milestone of the related issue.
For example, a PR to fix a bug for upcoming version 1.6.1 would merge into `beta/1.6.1`.

## Bug Fixes

When there's a bug, the target branch depends on which version introduced the bug.
Users should be able to get the bug fix as a patch version, without having to upgrade to the next major/minor version.
This means the bug fix may spawn multiple beta versions.

For example, if version 1.6.0 introduced a bug:

| Version      | Bug Fix Version |
|--------------|-----------------|
| 1.5.3        | none            |
| 1.6.1        | 1.6.2           |
| 1.7.0        | 1.7.1           |
| 2.0.0 (beta) | 2.0.0           |
