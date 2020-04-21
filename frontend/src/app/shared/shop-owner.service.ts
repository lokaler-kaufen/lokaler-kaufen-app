import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {LoginStateService} from './login-state.service';
import {TokenInfoDto} from '../data/api';
import {first} from 'rxjs/operators';

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
    // check the current status once and if we didn't find a token, try to get the tokenInfo from the backend
    this.loginStateService.isShopOwner
      .pipe(first())
      .subscribe(loggedIn => loggedIn || this.updateTokenInfo());
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
    return this.http.delete(API_SHOP_OWNER_LOGIN).toPromise()

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

  private updateTokenInfo() {
    this.http.get(API_SHOP_OWNER_TOKEN_INFO).toPromise()
      .then((tokenInfo: TokenInfoDto) => {
        if (tokenInfo.status === 'LOGGED_IN') {
          this.loginStateService.loginShopOwner(tokenInfo);

        } else {
          this.loginStateService.logoutShopOwner();
        }
      })
      .catch(() => this.loginStateService.logoutShopOwner());
  }


  /** @deprecated */
  storeOwnerLoggedIn(): void {
    // will set the login state to true if the user is actually logged in
    this.updateTokenInfo();
  }

}
