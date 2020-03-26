import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ShopAdminDto, ShopsAdminDto} from '../data/client';
import {NotificationsService} from 'angular2-notifications';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  shopDetails: ShopsAdminDto;

  constructor(private client: HttpClient,
              private notificationsService: NotificationsService) {
    this.client.get('/api/admin/shop').subscribe(response => {
      this.shopDetails = response;
    });
  }

  listAllShops() {
    return this.shopDetails;
  }

  getShopWithId(id: string) {
    return this.shopDetails.shops.find(shop => shop.id === id);
  }

  updateShop(updatedShop: ShopAdminDto) {
    this.client.put('/api/admin/shop/' + encodeURIComponent(updatedShop.id), updatedShop)
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

  changeShopEnable(shopId: string, enabled: boolean) {
    this.client.delete('/api/admin/shop/' + encodeURIComponent(shopId) + '/enable?enabled=' + enabled)
      .subscribe(() => {
          this.notificationsService.success('Alles klar!', 'Das enabled flag wurde auf ' + enabled + ' gesetzt.');
        },
        error => {
          console.log('Error updating shop: ' + error.status + ', ' + error.message);
          this.notificationsService.error('Tut uns leid!', 'Die Aktivierung des Ladens konnte nicht geändert werden.');
        });
  }

}
