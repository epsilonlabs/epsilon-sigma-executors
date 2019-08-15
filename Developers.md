# Versioning 

We use semantic versioning and SNAPSHOT as qualifier for the latest development version.

Tags are named v\[version\].


# Branching

We follow git-flow practices. There are two main branches:

- master: stable releases (i.e. versions with no qualifiers)
- develop: latest development version (i.e. SNAPSHOT qualifier)

When preparing for a release, a release candidate branch under `releases/rc-[version]` should be created.
In that branch the version should be bumped to \[version\]-rc.
After the release candidate have been finalized (i.e. update documentation, all version references, run integration tests), the version must be bumped to a stable version.
Then, it should me merged with the *master*, and a tag must be created for the version.
Creating and pushing the tag will signal de CI to release the stable version.
After release, the master branch must be merged back into develop, and the version bumped to the next patch.

## Bump verions
The bump version step can be automated using [bump2version]().
*bump2version* is a Python script that can bump project versions across multiple files.
In this case we want to update the version in the parent and module poms, and in the readme.

To use it you must have python installed in your system and get the package from pypi:

```
pip install --upgrade bump2version
```

### Release Candidate
After creating the rc branch, the version can be bumped to rc:

```
bump2version release
```

This will:
 - change `[version]-SNAPSHOT` to `[version]-rc`.
 - create a commit for the bump 

### Release

After the release candidate has been finalized, bump version to stable.
**Note:** The rc version might have patch version increases.

```
bump2version --tag release
```

This will:
 - change `[version]-rc` to `[version]`.
 - create a commit for the bump 
 - create a tag for `[version]`.

Afterwards, merge rc branch to master.

**REMEMBER TO PUSH THE TAG!**

### Develop

Merge master to develop and then bump to next version:

```
bump2version patch
```

This will:
 - change `{major}.{minor}.{patch}` to `{major}.{minor}.{patch+1}-SNAPSHOT`.
 - create a commit for the bump 

### Increase version

Within develop, it can be used to bump any of the segments of the version.

# Testing

Test classes must be tagged, via `@Tag` annotations, as either *unit* or *integration* tests.
This tags are used by CI to run different sets of tests for different branches.

# Continuous Integration

We use CircleCi for continuous integration.
The following configuration is in place:

- All branches run unit tests. No sources or javadoc are built
- Develop branch runs unit tests. Sources are built. Artifacts are deployed.
- Release candidate branch runs unit tests and integration tests. Sources and javadocs are built.
- Master branch does not run anything
- Tags build sources and javadocs, and deploy  

