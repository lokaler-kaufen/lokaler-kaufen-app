import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {of, ReplaySubject} from 'rxjs';
import {ShopOwnerDetailDto} from '../data/api';
import {UpdateShopData} from '../shop-details-config/shop-details-config.component';
import {ImageService} from '../shared/image.service';
import {ShopOwnerService} from '../shared/shop-owner.service';
import {AsyncNotificationService} from '../i18n/async-notification.service';

@Component({
  selector: 'shop-management-page',
  templateUrl: './shop-management-page.component.html',
  styleUrls: ['./shop-management-page.component.css']
})
export class ShopManagementPageComponent implements OnInit {

  constructor(
    private client: HttpClient,
    private router: Router,
    private notificationsService: AsyncNotificationService,
    private shopOwnerService: ShopOwnerService,
    private imageService: ImageService
  ) {
  }

  shopDetails: ReplaySubject<ShopOwnerDetailDto> = new ReplaySubject<ShopOwnerDetailDto>();
  getUpdatedDetails: ReplaySubject<boolean> = new ReplaySubject<boolean>();
  shopId: string;
  progress = 0;

  ngOnInit() {
    this.client.get<ShopOwnerDetailDto>('/api/shop/me')
      .subscribe((shopDetails: ShopOwnerDetailDto) => {
          this.shopId = shopDetails.id;
          this.shopDetails.next(shopDetails);
        },
        error => {
          this.handleError(error, true);
        });
  }

  updateShop($event: UpdateShopData) {
    if ($event.image) {
      this.updateImageAndDetails($event);

    } else if ($event.deleteImage) {
      this.imageService.delete()
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
    this.imageService.upload(image, progress => this.progress = progress)
      .then(() => {
        this.router.navigate(['shops/' + id]);
      })
      .catch(() => {
        this.notificationsService.error('Tut uns leid!', 'Ihr Logo konnte nicht hochgeladen werden.');
      });
  }

  private updateShopDto(updateShopData: UpdateShopData) {
    this.client.put('/api/shop', updateShopData.updateShopDto).subscribe(() => {
        this.router.navigate(['shops/' + updateShopData.id]);
      },
      error => {
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
