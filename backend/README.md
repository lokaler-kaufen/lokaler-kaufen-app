# Mercury backend

## Get an architectural overview

Start with looking at the `de.qaware.mercury.mercury.rest.shop.ShopController` class.

## Run it

### With gradle

```shell script
./gradlew bootRun
```

### With IntelliJ

Start the `main()` method of the `de.qaware.mercury.mercury.MercuryApplication` class.

## Developing

### Conventions

* Use `public` as few as possible
* Make stuff immutable (`@Value`). If you need a "setter", try `@With` on the field.
* Everything is non-null by default. If something is nullable, mark it with `@Nullable` (`org.springframework.lang.Nullable`)
* DTO classes stay in the `rest` package
* Entity classes stay in the `storage` package
* The service layer doesn't know the storage or the REST layer
* Use query parameters (`@RequestParam`) in URLs for optional parameters. If a parameter is required, better use a `@PathVariable`.
* Use `varchar` without length for string columns ([Details](https://wiki.postgresql.org/wiki/Don%27t_Do_This#Don.27t_use_varchar.28n.29_by_default)).

## How To's

### Login as Admin

```
curlie :8080/api/admin/login email=admin-1@localhost password=admin-1
```

The access token is set as a cookie.