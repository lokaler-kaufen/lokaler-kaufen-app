import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpEventType} from '@angular/common/http';
import {Router} from '@angular/router';
import {NotificationsService} from 'angular2-notifications';
import {of, ReplaySubject} from 'rxjs';
import {ShopOwnerDetailDto} from '../data/api';
import {UpdateShopData} from '../shop-details-config/shop-details-config.component';
import {catchError, map} from 'rxjs/operators';
import {ImageService} from '../shared/image.service';
import {ShopOwnerService} from '../shared/shop-owner.service';

@Component({
  selector: 'shop-management-page',
  templateUrl: './shop-management-page.component.html',
  styleUrls: ['./shop-management-page.component.css']
})
export class ShopManagementPageComponent implements OnInit {

  shopDetails: ReplaySubject<ShopOwnerDetailDto> = new ReplaySubject<ShopOwnerDetailDto>();

  getUpdatedDetails: ReplaySubject<boolean> = new ReplaySubject<boolean>();

  shopId: string;

  constructor(private client: HttpClient,
              private router: Router,
              private notificationsService: NotificationsService,
              private shopOwnerService: ShopOwnerService,
              private imageService: ImageService) {
  }

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

  progress: number = 0;

  updateShop($event: UpdateShopData) {
    if ($event.image) {
      this.updateImageAndDetails($event);
    } else {
      if ($event.deleteImage) {
        this.imageService.delete().toPromise().then(() => console.log('Image deleted.')).catch(error => {
          console.log('Could not delete image.');
          this.notificationsService.error('Tut uns Leid!', 'Wir konnten dein Bild leider nicht löschen.');
        });
      }
      this.updateShopDto($event);
    }
  }

  private updateImageAndDetails(updateShopData: UpdateShopData) {
    const image = updateShopData.image;
    const uploadData = new FormData();
    uploadData.append('file', image, image.name);
    this.imageService.upload(uploadData).pipe(
      map(event => {
        switch (event.type) {
          case HttpEventType.UploadProgress:
            this.progress = Math.round(event.loaded * 100 / event.total);
            break;
          case HttpEventType.Response:
            return event;
        }
      }),
      catchError((error: HttpErrorResponse) => {
        console.log(`${image.name} upload failed.`);
        this.notificationsService.error('Tut uns leid!', 'Ihr Logo konnte nicht hochgeladen werden.');
        return of(error);
      })).subscribe((event: any) => {
      if (event) {
        if (event instanceof HttpErrorResponse) {
          console.log(event);
          return;
        } else {
          this.router.navigate(['shops/' + updateShopData.id]);
        }
      }
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

    let notificationTitle = 'Tut uns leid!';
    let notificationText: string;

    if (error.status === 400 && error.error.code === 'LOCATION_NOT_FOUND') {
      notificationTitle = 'Ungültige PLZ';
      notificationText = 'Diese Postleitzahl kennen wir leider nicht, haben Sie sich vertippt?';
    } else if (wasInitialLoad) {
      notificationText = 'Es ist ein Fehler beim Laden der Details aufgetreten.';
    } else if (error.status === 401 || error.status === 403) {
      notificationTitle = 'Authentifizierungsfehler';
      notificationText = 'Es ist ein Fehler aufgetreten. Bitte melden Sie sich erneut an.';
      // not ideal but it gets the job done
      this.shopOwnerService.storeOwnerLoggedOut();
      this.router.navigate(['login']);
    } else {
      notificationText = 'Ihr Laden konnte leider nicht aktualisiert werden.';
    }

    this.notificationsService.error(notificationTitle, notificationText);
  }
}
