# Branching Model

The UDTopia Git repo uses a three-level stability model for branches.

| Branch Name  | Stability                        | Purpose                                 |
|--------------|----------------------------------|-----------------------------------------|
| `alpha`      | Least stable                     | Features for future releases.           |
| `beta/*.*.*` | Stable, but not production-ready | Stabilize the next release version.     |
| `stable`     | Production-ready                 | Released version, available on Central. |

## Pull Requests

The target branch for a pull request depends on the milestone of the related issue.
For example, a PR to fix a bug for upcoming version 1.6.1 would merge into `beta/1.6.1`.

## Bug Fixes and Security Patches

When there's a bug or a security vulnerability, the target branch depends on the affected version.
Users should be able to get the bug fix as a patch version, without having to upgrade to the next major/minor version.
This means the bug fix may spawn multiple beta versions.

For example, if version 1.6.0 introduced a bug or has a security vulnerability:

| Version      | Bug Fix Version | Remarks                                 |
|--------------|-----------------|-----------------------------------------|
| 1.5.3        | none            | Unaffected; no bug fix required.        |
| 1.6.1        | 1.6.2           | Next bug fix version for `1.6.*`.       |
| 1.7.0        | 1.7.1           | Next bug fix version for `1.7.*`.       |
| 2.0.0 (beta) | 2.0.0           | Not yet released; fix directly on beta. |
