# Mercury backend

## Run it

### With Docker Compose

(assuming you've already built the backend JAR with `./gradlew build`)

```shell script
docker-compose up
```

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

## Developing

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
* Don't write the constructors of your Spring beans by hand, use `@RequiredArgsConstructor(access = AccessLevel.PACKAGE)`
* When writing Spock tests, use `def` as little as possible (`def` breaks refactoring) 

## How To's

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

# Credits

For geolocation lookups we are using data from [GeoNames](https://download.geonames.org/export/zip/)
which is free to use under the [Creative Commons Attribution 3.0 License](https://creativecommons.org/licenses/by/3.0/).
