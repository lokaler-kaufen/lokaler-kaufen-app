import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ShopAdminDto, ShopsAdminDto} from '../data/api';
import {UpdateShopData} from '../shop-details-config/shop-details-config.component';

@Injectable({providedIn: 'root'})
export class AdminService {

  constructor(private client: HttpClient) {
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
