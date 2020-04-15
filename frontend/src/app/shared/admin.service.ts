import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ShopAdminDto, ShopsAdminDto, TokenInfoDto} from '../data/api';
import {UpdateShopData} from '../shop-details-config/shop-details-config.component';
import {Observable, Subject} from 'rxjs';

const API_ADMIN_TOKEN_INFO = '/api/admin/login/token-info';
const API_ADMIN_LOGIN = '/api/admin/login';

@Injectable({providedIn: 'root'})
export class AdminService {

  private loggedIn = new Subject<boolean>();

  constructor(private client: HttpClient) {
    this.client.get(API_ADMIN_TOKEN_INFO).toPromise()
      .then((response: TokenInfoDto) => {
        if (response.status === 'LOGGED_IN') {
          this.loggedIn.next(true);

        } else {
          this.loggedIn.next(false);
        }
      })
      .catch(() => this.loggedIn.next(false));
  }

  getAdminLoginState(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  onSuccessfulLogin() {
    this.loggedIn.next(true);
  }

  logout(): Promise<void> {
    return this.client.delete(API_ADMIN_LOGIN).toPromise()
      .then(() => console.log('Admin logout successful.'))
      .catch(error => console.error('Admin logout failed.', error))
      .finally(() => this.loggedIn.next(false));
  }

  listAllShops(): Promise<ShopsAdminDto> {
    return this.client.get('/api/admin/shop').toPromise();
  }

  getShopWithId(id: string): Promise<ShopAdminDto> {
    const shopId = encodeURIComponent(id);

    return this.client.get<ShopAdminDto>(`/api/admin/shop/${shopId}`).toPromise();
  }

  updateShop(updatedShop: UpdateShopData) {
    const shopId = encodeURIComponent(updatedShop.id);

    return this.client.put(`/api/admin/shop/${shopId}`, updatedShop.updateShopDto).toPromise();
  }

  deleteShop(id: string) {
    const shopId = encodeURIComponent(id);

    return this.client.delete(`/api/admin/shop/${shopId}`).toPromise();
  }

  changeShopApproval(id: string, enabled: boolean) {
    const shopId = encodeURIComponent(id);

    return this.client.put(`/api/admin/shop/${shopId}/approve?approved=${enabled}`, {}).toPromise();
  }

}
