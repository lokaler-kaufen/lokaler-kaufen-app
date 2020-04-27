import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {ReplaySubject} from 'rxjs';
import {ShopOwnerDetailDto} from '../data/api';
import {UpdateShopData} from '../shop-details-config/shop-details-config.component';
import {ShopImageClient} from '../api/image/shop-image.client';
import {ShopOwnerService} from '../shared/shop-owner.service';
import {AsyncNotificationService} from '../i18n/async-notification.service';
import {ShopOwnerClient} from '../api/shop/shop-owner.client';

@Component({
  selector: 'shop-management-page',
  templateUrl: './shop-management-page.component.html',
  styleUrls: ['./shop-management-page.component.css']
})
export class ShopManagementPageComponent implements OnInit {

  constructor(
    private client: ShopOwnerClient,
    private router: Router,
    private notificationsService: AsyncNotificationService,
    private shopOwnerService: ShopOwnerService,
    private shopImageClient: ShopImageClient
  ) {
  }

  shopDetails: ReplaySubject<ShopOwnerDetailDto> = new ReplaySubject<ShopOwnerDetailDto>();
  getUpdatedDetails: ReplaySubject<boolean> = new ReplaySubject<boolean>();
  shopId: string;
  progress = 0;

  ngOnInit() {
    this.client.getShopSettings()
      .then((shopDetails: ShopOwnerDetailDto) => {
        this.shopId = shopDetails.id;
        this.shopDetails.next(shopDetails);
      })
      .catch(error => {
        this.handleError(error, true);
      });
  }

  updateShop($event: UpdateShopData) {
    if ($event.image) {
      this.updateImageAndDetails($event);

    } else if ($event.deleteImage) {
      this.shopImageClient.deleteImageFromShop()
        .then(() => {
          this.updateShopDto($event);
        })
        .catch(() => {
          this.notificationsService.error('Tut uns Leid!', 'Wir konnten dein Bild leider nicht löschen.');
        });

    } else {
      this.updateShopDto($event);
    }
  }

  private updateImageAndDetails({id, image}: UpdateShopData) {
    this.shopImageClient.uploadImageForShop(image, progress => this.progress = progress)
      .then(() => {
        this.router.navigate(['shops/' + id]);
      })
      .catch(() => {
        this.notificationsService.error('Tut uns leid!', 'Ihr Logo konnte nicht hochgeladen werden.');
      });
  }

  private updateShopDto({id, updateShopDto}: UpdateShopData) {
    this.client.updateShop(updateShopDto)
      .then(() => {
        this.router.navigate(['shops/' + id]);
      })
      .catch(error => {
        this.handleError(error, false);
      });
  }

  private handleError(error: any, wasInitialLoad: boolean): void {
    const logPrefix: string = wasInitialLoad ? 'Error requesting shop details: ' : 'Error updating shop: ';
    console.log(logPrefix + error.status + ', ' + error.message + ', ' + error.error.code);

    let notificationTitle = 'shop.management.error.generic.title';
    let notificationText: string;

    if (error.status === 400 && error.error.code === 'LOCATION_NOT_FOUND') {
      notificationTitle = 'Ungültige PLZ';
      notificationText = 'Diese Postleitzahl kennen wir leider nicht, haben Sie sich vertippt?';

    } else if (wasInitialLoad) {
      notificationText = 'Es ist ein Fehler beim Laden der Details aufgetreten.';

    } else {
      notificationText = 'shop.management.error.update.message';
    }

    this.notificationsService.error(notificationTitle, notificationText);
  }

}
