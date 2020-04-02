import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {NotificationsService} from 'angular2-notifications';
import {ReplaySubject} from 'rxjs';
import {ShopOwnerDetailDto} from '../data/api';
import {UpdateShopData} from '../shop-details-config/shop-details-config.component';
import {UserContextService} from '../shared/user-context.service';

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
              private userContextService: UserContextService) {
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

  updateShop($event: UpdateShopData) {
    this.client.put('/api/shop', $event.updateShopDto).subscribe(() => {
        this.router.navigate(['shops/' + $event.id]);
      },
      error => {
        this.handleError(error, false);
      });
  }

  private handleError(error: any, wasInitialLoad: boolean): void {
    const logPrefix: string = wasInitialLoad ? 'Error requesting shop details: ' : 'Error updating shop: ';
    console.log(logPrefix + error.status + ', ' + error.message + ', ' + error.error.code);

    let notificationTitle  = 'Tut uns leid!';
    let notificationText: string;

    if (error.status === 400 && error.error.code === 'LOCATION_NOT_FOUND') {
      notificationTitle = 'Ungültige PLZ';
      notificationText =  'Diese Postleitzahl kennen wir leider nicht, haben Sie sich vertippt?';
    } else if (wasInitialLoad) {
      notificationText = 'Es ist ein Fehler beim Laden der Details aufgetreten.';
    } else if (error.status === 401 || error.status === 403) {
      notificationTitle = 'Authentifizierungsfehler';
      notificationText = 'Es ist ein Fehler aufgetreten. Bitte melden Sie sich erneut an.';
      // not ideal but it gets the job done
      this.userContextService.storeOwnerLoggedOut();
      this.router.navigate(['login']);
    } else {
      notificationText = 'Ihr Laden konnte leider nicht aktualisiert werden.';
    }

    this.notificationsService.error(notificationTitle, notificationText);
  }
}
