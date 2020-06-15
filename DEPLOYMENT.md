# Deployment

## Prerequisites

* Access to a user account on the target machine
* Public key configured on the target machine
* User needs to be part of the group `deployment`

*Hint*: See RELEASE.MD for details on how to create a new version.

### Database setup

In case of incompatible database changes, you need to remove all database tables **before** the deployment.
Connecting the database to IntelliJ is recommended.
Database credentials can be obtained from `Josef Fuchshuber`, `Florian Engel` or `Franz Wimmer`.

## Deployment script

Usage:

```
$ ./deploy.sh [-p] <username>
```

This will deploy to `TEST` ([https://test.lokaler.kaufen](https://test.lokaler.kaufen)).

The `-p` parameter changes the deployment target to `PROD` ([https://demo.lokaler.kaufen](https://demo.lokaler.kaufen)).

The build also execute all tests, which include the backend tests using "Testcontainers". If you're using podman instead of docker, make sure your PostgreSQL container is running as described in the backend documentation and execute:

```
$ MERCURY_NO_TESTCONTAINER=1 ./deploy.sh [-p] <username>
```
Keep in mind that the database does not reset automatically, so you have to restart the container after each build.

If you want to deploy from a system which does neither have docker nor podman, you can skip running the integration tests [NOT RECOMMENDED] by running:
```
$ GRADLE_OPTS="-DskipIntegrationTests" ./deploy.sh [-p] <username>
```
In this case, you have to make sure that no tests are failing some other way.
