# MercuryUi

## API Clients

Client (API-calling) code should **not** sit inside of UI components. Ideally, every Controller of the backend has a
corresponding Client in the frontend that provides access to the backend methods without leaking (too much) of the
transport tech stuff into the code that's using the client.

All client implementations should sit in `src/app/api`

Example: There is a `ShopImageController` in the backend So there needs to be a `ShopImageClient` in the frontend. This
client should be in a file called `src/app/api/shop/shop-image.client.ts` (matching class name, matching file name,
matching directory to mirror the backend structure).

Observables are nice and dandy but don't make any sense when we're performing one request that results in one response
(or one failure). Therefore, it's reasonable that users of these client classes want a Promise instead of an 
Observable. To help minimize boilerplate code while enabling consistent logging of errors, use the `wrapRequest`
helper function.

---

(original Angular Readme below)

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 9.0.5.

## Prerequisites
 * [NPM v12](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm#using-a-node-installer-to-install-node-js-and-npm) installed
 * [AngularCLI v9](https://angular.io/cli) installed (`npm install -g @angular/cli`)

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `--prod` flag for a production build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).
