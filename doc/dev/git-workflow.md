# Git workflow

## Create a new release

1. Ensure that all bugfix commits are on the master: `git checkout master && git pull && git merge bugfix-1.0.x`
1. Increase the version in the `build.gradle` to the next release version
    * e.g. if `1.0.1` is currently deployed, the next version is `1.1.0` and so forth
1. Commit the change: `git commit -a`
1. Tag this commit with the version number: `git tag v1.1.0`
1. Create the bugfix branch for that release: `git checkout -b bugfix-1.1.x`
    * When the released version is 1.1.0, the bugfix branch is called `bugfix-1.1.x` and so on
1. Increase the version in the `build.gradle` to the next bugfix version
    * e.g. if `1.1.0` is the next version on production, the next bugfix version is `1.1.1` and so forth
1. Commit the change: `git commit -a` 
1. Switch to `master` branch again: `git checkout master`
1. Increase the version in the `build.gradle` to the next development version
    * e.g. if `1.1.0` is the next version on production, the next development version is `1.2.0` and so forth
1. Commit the change: `git commit -a`
1. Do a no-op merge into the master: `git pull && git merge bugfix-1.1.x --strategy=ours --message="Formal no-op merge of version changes in bugfix"`
1. Push the `master` and tags: `git push && git push --tags`
1. Push the new bugfix branch: `git push -u origin bugfix-1.1.x`
1. Checkout the new release version: `git checkout v1.1.0`
1. Deploy the version to prod: `./deploy -p [username]`

## Fix a bug in the current deployed release

1. Check out the `bugfix-[version]` branch for the currently deployed release: `git checkout bugfix-1.0.x`
    * You can find out the version number of the currently deployed release like this: `curl https://app.lokaler.kaufen/api/info/version`
    * The branch is named `1.0.x` if the currently deployed release starts with `1.0.`
    * The branch is named `1.1.x` if the currently deployed release starts with `1.1.`
    * and so forth
1. Fix the bug
1. Commit it on the bugfix branch
1. Immediately merge the bugfix branch back into the master: `git checkout master && git pull && git merge bugfix-1.0.x`
1. Now your fix is on the bugfix branch and on the master 

## Roll out a bugfix for the deployed release

1. Ensure that all bugfix commits are on the master: `git checkout master && git pull && git merge bugfix-1.0.x`
1. Checkout the bugfix branch: `git checkout bugfix-1.0.x`
1. Ensure that it builds: `cd backend && ./gradlew build && cd ../frontend && ng build`
1. Increase the version in the `build.gradle` to the next bugfix version
    * e.g. if `1.0.1` is deployed, the next version is `1.0.2` and so forth
1. Commit the change on the bugfix branch
1. Tag this commit with the version number: `git tag v1.0.2`
1. Increase the version in the `build.gradle` to the new snapshot version
    * e.g. if the new version is `1.0.2`, the new snapshot version is `1.0.3-SNAPSHOT`
1. Commit the change on the bugfix branch
1. Do a no-op merge into the master: `git checkout master && git pull && git merge bugfix-1.0.2 --strategy=ours --message="Formal no-op merge of version changes in bugfix"`
1. Push the master: `git push`
1. Checkout the bugfix branch: `git checkout bugfix-1.0.x`
1. Push the bugfix branch and tags: `git push && git push --tags`
1. Checkout the new release version: `git checkout v1.0.2`
1. Deploy the version to prod: `./deploy -p [username]`
