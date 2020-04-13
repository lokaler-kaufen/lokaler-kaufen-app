import {Component, Injector, OnInit} from '@angular/core';
import {ReplaySubject} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {AdminService} from '../shared/admin.service';
import {NotificationsService} from 'angular2-notifications';
import {UpdateShopData} from '../shop-details-config/shop-details-config.component';
import {ShopAdminDto, ShopOwnerDetailDto} from '../data/api';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'admin-details-page',
  templateUrl: './admin-details-page.component.html',
  styleUrls: ['./admin-details-page.component.css']
})
export class AdminDetailsPageComponent implements OnInit {

  shopDetails: ShopAdminDto;
  shopId: string;

  shopOwnerDetails: ReplaySubject<ShopOwnerDetailDto> = new ReplaySubject<ShopOwnerDetailDto>();

  getUpdatedDetails: ReplaySubject<boolean> = new ReplaySubject<boolean>();

  constructor(private adminService: AdminService,
              private route: ActivatedRoute,
              private notificationsService: NotificationsService,
              private translateService: TranslateService) {
  }

  ngOnInit() {
    this.shopId = this.route.snapshot.paramMap.get('id');

    this.adminService.getShopWithId(this.shopId)
      .then(response => {
        this.shopDetails = response;
        this.shopOwnerDetails.next(this.extractShopOwnerDetailDto(this.shopDetails));
      })
      .catch(error => {
        console.log('Error requesting shop details: ' + error.status + ', ' + error.message);
        this.notificationsService.error('admin.details.error.title', 'Es ist ein Fehler beim Laden der Details aufgetreten.');
      });
  }

  updateShop($event: UpdateShopData) {
    this.adminService.updateShop($event)
      .then(() => this.notificationsService.success('Alles klar!', 'Ihr Laden wurde aktualisiert.'))
      .catch(error => {
        console.log('Error updating shop: ' + error.status + ', ' + error.message);
        this.notificationsService.error('admin.details.error.title', 'Ihr Laden konnte leider nicht aktualisiert werden.');
      });
  }

  deleteShop() {
    this.adminService.deleteShop(this.shopId)
      .then(() => this.notificationsService.success('Alles klar!', 'Ihr Laden wurde gelöscht.'))
      .catch(error => {
        console.log('Error updating shop: ' + error.status + ', ' + error.message);
        this.notificationsService.error('admin.details.error.title', 'Ihr Laden konnte leider nicht gelöscht werden.');
      });
  }

  changeShopApproval() {
    const nextValue = !this.shopDetails.approved;

    this.adminService.changeShopApproval(this.shopId, nextValue)
      .then(() => this.notificationsService.success('Alles klar!', `Das approved flag wurde auf ${nextValue} gesetzt.`))
      .catch(error => {
        console.log('Error updating shop: ' + error.status + ', ' + error.message);
        this.notificationsService.error('admin.details.error.title', 'Die Aktivierung Ihres Ladens konnte nicht geändert werden.');
      })
      .finally(() => this.shopDetails.approved = nextValue);
  }

  extractShopOwnerDetailDto(shopAdminDto: ShopAdminDto): ShopOwnerDetailDto {
    return shopAdminDto as ShopOwnerDetailDto;
  }

}
