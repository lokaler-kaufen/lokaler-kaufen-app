import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ShopAdminDto, ShopsAdminDto, TokenInfoDto} from '../data/api';
import {UpdateShopData} from '../shop-details-config/shop-details-config.component';
import {Observable} from 'rxjs';
import {LoginStateService} from './login-state.service';

const API_ADMIN = '/api/admin';
const API_ADMIN_LOGIN = `${API_ADMIN}/login`;
const API_ADMIN_TOKEN_INFO = `${API_ADMIN}/login/token-info`;

@Injectable({providedIn: 'root'})
export class AdminService {

  constructor(private client: HttpClient, private loginStateService: LoginStateService) {
    this.client.get(API_ADMIN_TOKEN_INFO).toPromise()
      .then((response: TokenInfoDto) => {
        if (response.status === 'LOGGED_IN') {
          this.loginStateService.loginAdmin();

        } else {
          this.loginStateService.logoutAdmin();
        }
      })
      .catch(() => this.loginStateService.logoutAdmin());
  }

  getAdminLoginState(): Observable<boolean> {
    return this.loginStateService.isAdmin;
  }

  onSuccessfulLogin() {
    this.loginStateService.loginAdmin();
  }

  logout(): Promise<void> {
    return this.client.delete(API_ADMIN_LOGIN).toPromise()
      .then(() => console.log('Admin logout successful.'))
      .catch(error => console.error('Admin logout failed.', error))
      .finally(() => this.loginStateService.logoutAdmin());
  }

  listAllShops(): Promise<ShopsAdminDto> {
    return this.client.get(`${API_ADMIN}/shop`).toPromise();
  }

  getShopWithId(id: string): Promise<ShopAdminDto> {
    const shopId = encodeURIComponent(id);

    return this.client.get<ShopAdminDto>(`${API_ADMIN}/shop/${shopId}`).toPromise();
  }

  updateShop(updatedShop: UpdateShopData) {
    const shopId = encodeURIComponent(updatedShop.id);

    return this.client.put(`${API_ADMIN}/shop/${shopId}`, updatedShop.updateShopDto).toPromise();
  }

  deleteShop(id: string) {
    const shopId = encodeURIComponent(id);

    return this.client.delete(`${API_ADMIN}/shop/${shopId}`).toPromise();
  }

  changeShopApproval(id: string, enabled: boolean) {
    const shopId = encodeURIComponent(id);

    return this.client.put(`${API_ADMIN}/shop/${shopId}/approve?approved=${enabled}`, {}).toPromise();
  }

}
