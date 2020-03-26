import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {NotificationsService} from 'angular2-notifications';
import {ShopOwnerDetailDto, UpdateShopDto} from '../data/client';
import {ReplaySubject} from 'rxjs';

@Component({
  selector: 'shop-management-page',
  templateUrl: './shop-management-page.component.html',
  styleUrls: ['./shop-management-page.component.css']
})
export class ShopManagementPageComponent implements OnInit {

  shopDetails: ReplaySubject<ShopOwnerDetailDto> = new ReplaySubject<ShopOwnerDetailDto>();
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

  updateShop($event: UpdateShopDto) {
    this.client.put('/api/shop', $event).subscribe(() => {
        this.router.navigate(['shops/' + this.shopId]);
      },
      error => {
        console.log('Error updating shop: ' + error.status + ', ' + error.message);
        this.notificationsService.error('Tut uns leid!', 'Dein Laden konnte leider nicht aktualisiert werden.');
      });
  }


}
