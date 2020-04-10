# Mercury backend

## Preconditions

* `docker` or `podman` to run the PostgreSQL database
* `GraphicsMagick` to process uploaded images
  * Mac: `https://formulae.brew.sh/formula/graphicsmagick`
  * Linux: `dnf install GraphicsMagick`

### With gradle

First, start a local postgres database. Easiest way to do this is with docker:

```shell script
docker run --rm -e POSTGRES_PASSWORD=password -p 5432:5432 postgres:11
```

Then run the application with Gradle:

```shell script
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### With IntelliJ

You also need a running PostgreSQL database, see paragraph above.

Then start the `main()` method of the `de.qaware.mercury.MercuryApplication` class. Make sure you pass `--spring.profiles.active=dev`
to the application to start in `dev` profile.

### With Docker Compose

(assuming you've already built the backend JAR with `./gradlew build`)

```shell script
docker-compose up --build
```

## Developing

### Run the build

If you have docker installed, just run

```shell script
./gradlew build
```

If you want to skip all tests, run gradle with `-x test`:

```
./gradlew build -x test
```

To skip only the integration tests, run gradle with `-DskipIntegrationTests`:

```
./gradlew build -DskipIntegrationTests
```

If you have a modern linux which docker doesn't support and you normally run your containers with podman, 
Testcontainers (which we use for integration testing) unfortunately won't work.

To run the integration tests on such a system, a running PostgreSQL database must be provided on port 5432 with credentials 
`postgres:password`. Easiest way to do that is with podman:

```
podman run --rm -e POSTGRES_PASSWORD=password -p 5432:5432 postgres:11
```

Then set the environment variable `MERCURY_NO_TESTCONTAINER=1` and run the build:

```shell script
MERCURY_NO_TESTCONTAINER=1 ./gradlew build
```

Keep in mind that the database needs to be restarted after each build since it will preserve all changes made by the tests and run into key constraint violations on the next test run.

### Get an architectural overview

Start with looking at the `de.qaware.mercury.rest.shop.ShopController` class.

### Conventions

* Use `public` as few as possible
* Make stuff immutable (`@Value`). If you need a "setter", try `@With` on the field.
* Everything is non-null by default. If something is nullable, mark it with `@Nullable` (`org.springframework.lang.Nullable`)
* DTO classes stay in the `rest` package
* Entity classes stay in the `storage` package
* The business layer doesn't know the storage or the REST layer
* Use query parameters (`@RequestParam`) in URLs for optional parameters. If a parameter is required, better use a `@PathVariable`.
* Use `varchar` without length for string columns ([Details](https://wiki.postgresql.org/wiki/Don%27t_Do_This#Don.27t_use_varchar.28n.29_by_default)).
* Use `@Transactional` only in the business layer
* Don't use `Instant.now()`, `LocalDateTime.now()` etc. These are not mockable in tests. Use the `Clock` interface instead.
* Don't use `Random` etc. These are not mockable in tests. Use the `RNG` interface instead.
* Don't write the constructors of your Spring beans by hand, use `@RequiredArgsConstructor(access = AccessLevel.PACKAGE)`

### Conventions for tests

* Use Spock, not JUnit 
* When writing Spock tests, use `def` as little as possible (`def` breaks refactoring) 
* When writing Spock tests, annotate the class with `@TypeChecked` if possible (this enables strict type checking in Groovy)
* When writing integration tests, annotate the tests with `@IntegrationTest`

## How To's

### What are the default logins?

If you start the application in the `dev` profile, it will create some admin accounts and some shops:

|Type   |Email              |Password   |
|-------|-------------------|-----------|
|Admin  |admin-1@local.host |admin-1    |
|Admin  |admin-2@local.host |admin-2    |
|Shop   |moe@local.host     |moe        |
|Shop   |flo@local.host     |flo        |
|Shop   |vroni@local.host   |vroni      |

### Add a column to existing database tables

1. Add column with `DEFAULT` and a sane value for the existing rows
1. Drop the `DEFAULT` on the column

```
ALTER TABLE shop
    ADD COLUMN approved boolean NOT NULL DEFAULT true;
ALTER TABLE shop 
    ALTER COLUMN approved DROP DEFAULT;
```

### Get rid of all data in the postgres

Just kill the docker container (pressing Ctrl+C in that terminal), and start it again.

### Create a DTO which Spring deserializes from JSON

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String email;
    private String password;
}
```

### See the API

Open http://localhost:8080/swagger-ui.html or take a look at the [src/test/http](src/test/http) folder.

### Fix strange errors on startup

```
Caused by: java.lang.IllegalArgumentException: URL must start with 'jdbc'
```

Solution:

If you are developing on the application, start the application in `dev` profile, see above.

If that happens on your server, you have to configure a database, see [the settings documentation](settings.md).

### Add a new admin

Start the application with the `--add-admin` flag. The application will then prompt for an email and a password,
creates the admin and shuts down again. This can be done while another instance of the application is running.

### Where's the documentation for Spock?

As we use a milestone release of Spock 2.0 to get support for JUnit 5, the documentation is somewhat hard to find.
It's [here](http://spockframework.org/spock/docs/2.0-M2/all_in_one.html).

# Credits

For geolocation lookups we are using data from [GeoNames](https://download.geonames.org/export/zip/)
which is free to use under the [Creative Commons Attribution 3.0 License](https://creativecommons.org/licenses/by/3.0/).
