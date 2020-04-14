import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Router} from '@angular/router';
import {AdminService} from '../shared/admin.service';
import {ShopOwnerService} from '../shared/shop-owner.service';

/**
 * All 401'ed requests belonging to this base URL will trigger a redirect to the admin login page.
 */
const API_ADMIN_BASE = '/api/admin';

/**
 * These URLs will NOT trigger a redirect to the admin login page.
 */
const API_ADMIN_WHITELIST = [
  '/api/admin/login'
];

/**
 * All 401'ed requests belonging to this base URL will trigger a redirect to the shop owner login page.
 */
const API_SHOP_OWNER_BASE = '/api/shop';

/**
 * These URLs will NOT trigger a redirect to the shop owner login page.
 */
const API_SHOP_OWNER_WHITELIST = [
  '/api/shop/login'
];

@Injectable()
export class LogoutInterceptor implements HttpInterceptor {

  constructor(
    private router: Router,
    private adminService: AdminService,
    private shopOwnerService: ShopOwnerService
  ) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error?.status === 401) {
          // some "unauthorized" call, alright
          const {url} = request;

          if (this.wasAdminRequest(url)) {
            // redirect to admin login page
            this.adminService.forceLogoutState();
            this.router.navigate(['/admin']);

          } else if (this.wasShopOwnerRequest(url)) {
            // redirect to shop owner login page
            this.shopOwnerService.forceLogoutState();
            this.router.navigate(['/login']);
          }
        }

        return throwError(error);
      })
    );
  }

  private wasAdminRequest(url: string): boolean {
    return url.startsWith(API_ADMIN_BASE) && !API_ADMIN_WHITELIST.some(wl => wl === url);
  }

  private wasShopOwnerRequest(url: string): boolean {
    return url.startsWith(API_SHOP_OWNER_BASE) && !API_SHOP_OWNER_WHITELIST.some(wl => wl === url);
  }

}
