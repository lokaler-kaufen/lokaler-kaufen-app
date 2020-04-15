import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {LoginStateService} from './login-state.service';
import {TokenInfoDto} from '../data/api';

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface ResetPasswordBody {
  email: string;
}

const API_SHOP_OWNER_TOKEN_INFO = '/api/shop/login/token-info';
const API_SHOP_OWNER_LOGIN = '/api/shop/login';
const API_SHOP_OWNER_PASSWORD_RESET = '/api/shop/send-password-reset-link';

/**
 * Service dealing with shop owner sessions.
 */
@Injectable({providedIn: 'root'})
export class ShopOwnerService {

  constructor(private http: HttpClient, private loginStateService: LoginStateService) {
    // call token-info to determine the initial state on page load
    this.http.get(API_SHOP_OWNER_TOKEN_INFO).toPromise()
      .then((response: TokenInfoDto) => {
        if (response.status === 'LOGGED_IN') {
          this.loginStateService.loginShopOwner();

        } else {
          this.loginStateService.logoutShopOwner();
        }
      })
      .catch(() => this.loginStateService.logoutShopOwner());
  }

  get shopOwnerLoggedIn(): Observable<boolean> {
    return this.loginStateService.isShopOwner;
  }

  login(credentials: LoginCredentials) {
    return this.http.post(API_SHOP_OWNER_LOGIN, credentials).toPromise()

      .then(() => console.log('[ShopOwnerService] Login successful.'))

      .catch(error => {
        console.error('[ShopOwnerService] Login failed.', error);
        this.loginStateService.logoutShopOwner();
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

      .finally(() => this.loginStateService.logoutShopOwner());
  }

  resetPassword(body: ResetPasswordBody) {
    return this.http.post(API_SHOP_OWNER_PASSWORD_RESET, body).toPromise()

      .then(() => console.log('[ShopOwnerService] Password reset successful.'))

      .catch(error => {
        console.error('[ShopOwnerService] Password reset failed.', error);
        throw error;
      });
  }


  /** @deprecated */
  storeOwnerLoggedIn(): void {
    this.loginStateService.loginShopOwner();
  }

  /** @deprecated */
  storeOwnerLoggedOut(): void {
    this.loginStateService.logoutShopOwner();
  }

}
