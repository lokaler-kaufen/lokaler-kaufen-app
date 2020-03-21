# API

1. Start the backend, see [this document](backend/README.md)
1. Open http://localhost:8080/swagger-ui.html in the browser

## Authorization

See the APIs for [admins](http://localhost:8080/swagger-ui.html#/admin-login-controller/loginUsingPOST) and [shops](http://localhost:8080/swagger-ui.html#/shop-login-controller/loginUsingPOST_1).
These methods set a cookie, which is sent back on every subsequent request.

You can call [this admin](http://localhost:8080/swagger-ui.html#/admin-login-controller/whoamiUsingGET) and [this shop](http://localhost:8080/swagger-ui.html#/shop-login-controller/whoamiUsingGET_1) API to test the authentication.
