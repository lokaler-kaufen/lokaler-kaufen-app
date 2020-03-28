import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NotificationsService} from 'angular2-notifications';
import {Observable} from 'rxjs';
import {ShopsAdminDto} from '../data/client/model/shopsAdminDto';
import {ShopAdminDto} from '../data/client/model/shopAdminDto';
import {UpdateShopData} from '../shop-details/shop-details.component';

@Injectable({
  providedIn: 'root'
})
export class AdminService {


  constructor(private client: HttpClient,
              private notificationsService: NotificationsService) {
  }

  listAllShops(): Observable<ShopsAdminDto> {
    return this.client.get('/api/admin/shop');
  }

  getShopWithId(id: string): Observable<ShopAdminDto> {
    return this.client.get<ShopAdminDto>('/api/admin/shop/' + encodeURIComponent(id));
  }

  updateShop(updatedShop: UpdateShopData) {
    this.client.put('/api/admin/shop/' + encodeURIComponent(updatedShop.id), updatedShop.updateShopDto)
      .subscribe(() => {
          this.notificationsService.success('Alles klar!', 'Der Laden wurde aktualisiert.');
        },
        error => {
          console.log('Error updating shop: ' + error.status + ', ' + error.message);
          this.notificationsService.error('Tut uns leid!', 'Der Laden konnte leider nicht aktualisiert werden.');
        });
  }

  deleteShop(shopId: string) {
    this.client.delete('/api/admin/shop/' + encodeURIComponent(shopId))
      .subscribe(() => {
          this.notificationsService.success('Alles klar!', 'Der Laden wurde gelöscht.');
        },
        error => {
          console.log('Error updating shop: ' + error.status + ', ' + error.message);
          this.notificationsService.error('Tut uns leid!', 'Der Laden konnte leider nicht gelöscht werden.');
        });
  }

  changeShopApproval(shopId: string, enabled: boolean) {
    this.client.put('/api/admin/shop/' + encodeURIComponent(shopId) + '/approve?approved=' + enabled, {})
      .subscribe(() => {
          this.notificationsService.success('Alles klar!', 'Das approved flag wurde auf ' + enabled + ' gesetzt.');
        },
        error => {
          console.log('Error updating shop: ' + error.status + ', ' + error.message);
          this.notificationsService.error('Tut uns leid!', 'Die Aktivierung des Ladens konnte nicht geändert werden.');
        });
  }

}
