# Release

This document contains the necessary information on how to create lokaler.kaufen releases. The release process is a set of conventions. These conventions guarantee a consistent git flow, and minimize merging and cherry-picking.

## Branch Layout
There are two important branches in our branch layout: `master` and `bugfix`. The `master` branch contains the snapshot of the next major or minor feature release (e.g. 1.3-SNAPSHOT or 2.0-SNAPSHOT), according to semantic versioning. While the actual development is mostly happening on short-living feature branches, developers are strongly encouraged to often integrate their feature branches into the `master` branch. The usual development rules (no failed tests, [no Sonar violations](https://sonarcloud.io/dashboard?id=qaware_wir-vs-virus), ...) apply.

Every minor version has its own feature branch. The bugfix branch of version 1.3 is `bugfix-1.3.x` and contains every bugfix version starting from 1.3.1.

Version changes on those bugfix branches are no-op merged into the master (`strategy=ours`). As a result, the bugfix branches can always be safely merged into the master and there is no need for cherry-picking bugfixes.

## Master Release
For the purpose of the examples in this chapter, we'll assume we want to release version 1.3.0.

1. Checkout the master branch and make sure the latest bugfix branch is merged into the master.

```
# assuming bugfix-1.2.x is the latest bugfix branch
$ git checkout master
$ git merge bugfix-1.2.x
```

2. Adjust the version in the build.gradle file in backend/ to the desired release version. This usually means removing the `-SNAPSHOT` suffix from the current version string.

Example:
```
- version = '1.3.0-SNAPSHOT'
+ version = '1.3.0'
```

3. Create version commit

```
$ git add backend/build.gradle
$ git commit -m "Set version to 1.3.0"
```

4. Tag version commit

```
$ git tag v1.3.0
```

5. Create a bugfix branch for the new version

```
git checkout -b bugfix-1.3.x
```

6. Adjust the version in the build.gradle file in backend/ to the next bugfix snapshot. This usually means increasing the bugfix part of the version number and adding the `-SNAPSHOT` suffix to the current version string.

Example:

```
- version = '1.3.0'
+ version = '1.3.1-SNAPSHOT'
```

7. Create snapshot version commit on bugfix-branch

```
$ git add backend/build.gradle
$ git commit -m "Set version to 1.3.1-SNAPSHOT"
```

8. Check out master branch

```
$ git checkout master
```

9. Adjust the version in the build.gradle file in backend/ to the next master snapshot. This usually means increasing the minor part of the version number and adding the `-SNAPSHOT` suffix to the current version string.

Example:

```
- version = '1.3.0'
+ version = '1.4.0-SNAPSHOT'
```


10. Create snapshot version commit on master

```
$ git add backend/build.gradle
$ git commit -m "Set version to 1.4.0-SNAPSHOT"
```

11. Perform the formal no-op merge of the version changes in the bugfix branch. Adjust the commit message of the merge commit to reflect its no-op nature, e.g. 'Formal no-op merge of version changes in bugfix branch'.

```
$ git merge bugfix-1.3.x --strategy=ours
```

12. Push master and tags

```
$ git push
$ git push --tags
```

13. Checkout and push bugfix branch

```
$ git checkout bugfix-1.3.x
$ git push
```

(This very first push on a new branch might fail because no proper upstream is set. In this case set the upstream branch like this:)

```
git push --set-upstream origin bugfix-1.3.x
```

14. (optional) if you want to deploy the new version you can do it like this:

```
$ git checkout v1.3.0
$ ./deploy.sh
```

Check DEPLOYMENT.MD for further information.

## Bugfix Release
For the purpose of the examples in this chapter, we'll assume we want to release version 1.3.1. The bugfix release is similar to the master release, but we don't need to create a new bugfix branch and we'll just create version changes on the current bugfix branch.

1. Checkout the master branch and make sure the latest bugfix branch is merged into the master.

```
# assuming bugfix-1.3.x is the latest bugfix branch
$ git checkout master
$ git merge bugfix-1.3.x
```

2. Checkout the bugfix branch

```
$ git checkout bugfix-1.3.x
```

3. Adjust the version in the build.gradle file in backend/ to the desired bugfix release version. This usually means removing the `-SNAPSHOT` from the current version string.

Example:
```
- version = '1.3.1-SNAPSHOT'
+ version = '1.3.1'
```

4. Create version commit

```
$ git add backend/build.gradle
$ git commit -m "Set version to 1.3.1"
```

5. Tag version commit

```
$ git tag v1.3.1
```

6. Adjust the version in the build.gradle file in backend/ to the next bugfix snapshot. This usually means increasing the bugfix part of the version number and adding the `-SNAPSHOT` suffix to the current version string.

Example:

```
- version = '1.3.1'
+ version = '1.3.2-SNAPSHOT'
```

7. Create snapshot version commit on bugfix-branch

```
$ git add backend/build.gradle
$ git commit -m "Set version to 1.3.2-SNAPSHOT"
```

8. Check out master branch

```
$ git checkout master
```

9. Perform the formal no-op merge of the version changes in the bugfix branch. Adjust the commit message of the merge commit to reflect its no-op nature, e.g. 'Formal no-op merge of version changes in bugfix branch'.

```
$ git merge bugfix-1.3.x --strategy=ours
```

10. Push master

```
$ git push
```

11. Checkout and push bugfix branch and tags

```
$ git checkout bugfix-1.3.x
$ git push
$ git push --tags
```

12. (optional) if you want to deploy the new version you can do it like this:

```
$ git checkout v1.3.1
$ ./deploy.sh
```

Check DEPLOYMENT.MD for further information.
