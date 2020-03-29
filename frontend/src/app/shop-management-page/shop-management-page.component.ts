import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {NotificationsService} from 'angular2-notifications';
import {ReplaySubject} from 'rxjs';
import {ShopOwnerDetailDto} from '../data/api';
import {UpdateShopData} from '../shop-details-config/shop-details-config.component';

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
              private notificationsService: NotificationsService) {
  }

  ngOnInit() {
    this.client.get<ShopOwnerDetailDto>('/api/shop/me')
      .subscribe((shopDetails: ShopOwnerDetailDto) => {
          this.shopId = shopDetails.id;
          this.shopDetails.next(shopDetails);
        },
        error => {
          console.log('Error requesting shop details: ' + error.status + ', ' + error.message);
          this.notificationsService.error('Tut uns leid!', 'Es ist ein Fehler beim Laden der Details aufgetreten.');
        });
  }

  updateShop($event: UpdateShopData) {
    this.client.put('/api/shop', $event.updateShopDto).subscribe(() => {
        this.router.navigate(['shops/' + $event.id]);
      },
      error => {
        console.log('Error updating shop: ' + error.status + ', ' + error.message + ', ' + error.error.code);
        if (error.status === 400 && error.error.code === 'LOCATION_NOT_FOUND') {
          this.notificationsService.error('Ungültige PLZ', 'Diese Postleitzahl kennen wir leider nicht, hast du dich vertippt?');
        } else {
          this.notificationsService.error('Tut uns leid!', 'Dein Laden konnte leider nicht aktualisiert werden.');
        }
      });
  }

}
