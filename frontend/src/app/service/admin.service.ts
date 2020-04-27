import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ShopAdminDto, ShopsAdminDto, TokenInfoDto} from '../data/api';
import {UpdateShopData} from '../shop-details-config/shop-details-config.component';
import {Observable} from 'rxjs';
import {LoginStateService} from '../shared/login-state.service';
import {first} from 'rxjs/operators';
import {ShopAdminClient} from '../api/shop/shop-admin.client';

const API_ADMIN = '/api/admin';
const API_ADMIN_LOGIN = `${API_ADMIN}/login`;
const API_ADMIN_TOKEN_INFO = `${API_ADMIN}/login/token-info`;

@Injectable({providedIn: 'root'})
export class AdminService {

  constructor(
    private client: HttpClient,
    private loginStateService: LoginStateService,
    private shopAdminClient: ShopAdminClient) {
    // check the current status once and if we didn't find a token, try to get the tokenInfo from the backend
    this.loginStateService.isAdmin
      .pipe(first())
      .subscribe(loggedIn => loggedIn || this.updateTokenInfo());
  }

  getAdminLoginState(): Observable<boolean> {
    return this.loginStateService.isAdmin;
  }

  onSuccessfulLogin() {
    // will set the login state to true if the user is actually logged in
    this.updateTokenInfo();
  }

  logout(): Promise<void> {
    return this.client.delete(API_ADMIN_LOGIN).toPromise()
      .then(() => console.log('Admin logout successful.'))
      .catch(error => console.error('Admin logout failed.', error))
      .finally(() => this.loginStateService.logoutAdmin());
  }

  listAllShops(): Promise<ShopsAdminDto> {
    return this.shopAdminClient.listAll();
  }

  getShopWithId(id: string): Promise<ShopAdminDto> {
    return this.shopAdminClient.getShopSettings(id);
  }

  updateShop({id, updateShopDto}: UpdateShopData) {
    return this.shopAdminClient.update(id, updateShopDto);
  }

  deleteShop(id: string) {
    return this.shopAdminClient.delete(id);
  }

  changeShopApproval(id: string, enabled: boolean) {
    return this.shopAdminClient.changeApprove(id, enabled);
  }

  private updateTokenInfo() {
    this.client.get(API_ADMIN_TOKEN_INFO).toPromise()
      .then((tokenInfo: TokenInfoDto) => {
        if (tokenInfo.status === 'LOGGED_IN') {
          this.loginStateService.loginAdmin(tokenInfo);

        } else {
          this.loginStateService.logoutAdmin();
        }
      })
      .catch(() => this.loginStateService.logoutAdmin());
  }

}
