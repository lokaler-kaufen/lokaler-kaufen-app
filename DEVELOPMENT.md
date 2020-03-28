# Development

## Prerequisites

Before you begin development, make sure you local environment is setup properly. For the development of the backend and frontend you will need
the following software components installed:

 * [Node.js](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm#using-a-node-installer-to-install-node-js-and-npm) installed (ideally 12.16.1 along with a somewhat up-to-date version of npm)
 * [AngularCLI v9](https://angular.io/cli) installed (`npm install -g @angular/cli`)
 * [Java 11](https://adoptopenjdk.net) installed, or use tools like `SdkMan` or `Jabba`
 * [Docker](https://www.docker.com/products/docker-desktop) if you want to build Docker images (optional)
 * [Tilt](https://tilt.dev) if you want to frontend and backend together for continuous development (optional)

## Frontend

To develop and run the frontend, you can simply run the following command. It will watch for any change to the app sources.
```
$ ng serve --proxy-config proxy.conf.json
```

## Backend Development

To develop and run the backend, you can simply spin up a Postgres instance using Docker and start the application using Gradle.

```
$ docker run --rm -d -e POSTGRES_PASSWORD=password -p 5432:5432 postgres:11
$ ./gradlew bootRun --args='--spring.profiles.active=dev'
```

Alternatively, you can use Docker Compose to fire up the database and
the backend service container together.

```
$ docker-compose up --build
```

## Continuous Development

By using the `Tilt` tool you can have the frontend, backend and the database running at the same time locally. The process will watch for file system changes and apply and deploy the automatically.
```
$ tilt up
```