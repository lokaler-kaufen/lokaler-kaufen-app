import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {NEVER, Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Router} from '@angular/router';
import {LoginStateService} from '../shared/login-state.service';
import {NotificationsService} from 'angular2-notifications';

/**
 * All 401'ed requests belonging to this base URL will trigger a redirect to the admin login page.
 */
const API_ADMIN_BASE = '/api/admin';

/**
 * Admin "whoami" call (ignored because we're doing it during the app startup already).
 */
const API_ADMIN_WHOAMI = '/api/admin/login';

/**
 * Shop owner "whoami" call (ignored because we're doing it during the app startup already).
 */
const API_SHOP_WHOAMI = '/api/shop/login';

@Injectable()
export class LogoutInterceptor implements HttpInterceptor {

  private static wasWhoamiRequest({url, method}: HttpRequest<unknown>): boolean {
    return method === 'GET' && (url.startsWith(API_ADMIN_WHOAMI) || url.startsWith(API_SHOP_WHOAMI));
  }

  private static wasAdminRequest(url: string): boolean {
    return url.startsWith(API_ADMIN_BASE);
  }

  constructor(
    private router: Router,
    private loginStateService: LoginStateService,
    private notificationsService: NotificationsService
  ) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error?.status === 401) {
          // some "unauthorized" call, alright
          const {url} = request;

          if (LogoutInterceptor.wasWhoamiRequest(request)) {
            // simply ignore the failed whoami requests

          } else if (LogoutInterceptor.wasAdminRequest(url)) {
            this.notificationsService.alert(
              'Ausgeloggt',
              'Ihre Login-Session ist vermutlich abgelaufen. Bitte loggen Sie sich noch einmal ein.'
            );

            this.loginStateService.logoutAdmin();

            // redirect to admin login page
            this.router.navigateByUrl('/admin');

          } else {
            this.notificationsService.alert(
              'Ausgeloggt',
              'Ihre Login-Session ist vermutlich abgelaufen. Bitte loggen Sie sich noch einmal ein.'
            );

            this.loginStateService.logoutShopOwner();

            // redirect to shop owner login page
            this.router.navigateByUrl('/login');
          }

          // don't want to re-throw an error which might cause error messages to pop up
          return NEVER;
        }

        return throwError(error);
      })
    );
  }

}
