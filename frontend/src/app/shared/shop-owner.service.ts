import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {HttpClient} from '@angular/common/http';

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface ResetPasswordBody {
  email: string;
}

const API_LOGIN_SHOP_OWNER = '/api/shop/login';
const API_RESET_SHOP_OWNER_PASSWORD = '/api/shop/send-password-reset-link';

/**
 * Service dealing with shop owner sessions.
 */
@Injectable({providedIn: 'root'})
export class ShopOwnerService {

  private loggedIn = new Subject<boolean>();

  constructor(private http: HttpClient) {
    // call whoami to determine the initial state on page load
    this.http.get(API_LOGIN_SHOP_OWNER).toPromise()
      .then(() => this.loggedIn.next(true))
      .catch(() => this.loggedIn.next(false));
  }

  get shopOwnerLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  login(credentials: LoginCredentials) {
    return this.http.post(API_LOGIN_SHOP_OWNER, credentials).toPromise()

      .then(() => console.log('[ShopOwnerService] Login successful.'))

      .catch(error => {
        console.error('[ShopOwnerService] Login failed.', error);
        this.loggedIn.next(false);
        throw error;
      });
  }

  logout() {
    return this.http.delete('/api/shop/login').toPromise()

      .then(() => console.log('[ShopOwnerService] Logout successful.'))

      .catch(error => {
        console.error('[ShopOwnerService] Logout failed.', error);
        throw error;
      })

      .finally(() => this.loggedIn.next(false));
  }

  resetPassword(body: ResetPasswordBody) {
    return this.http.post(API_RESET_SHOP_OWNER_PASSWORD, body).toPromise()

      .then(() => console.log('[ShopOwnerService] Password reset successful.'))

      .catch(error => {
        console.error('[ShopOwnerService] Password reset failed.', error);
        throw error;
      });
  }


  /** @deprecated */
  storeOwnerLoggedIn(): void {
    this.loggedIn.next(true);
  }

  /** @deprecated */
  storeOwnerLoggedOut(): void {
    this.loggedIn.next(false);
  }

}
