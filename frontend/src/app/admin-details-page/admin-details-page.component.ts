import {Component, OnInit} from '@angular/core';
import {ReplaySubject} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {AdminService} from '../shared/admin.service';
import {NotificationsService} from 'angular2-notifications';
import {ShopAdminDto} from '../data/client/model/shopAdminDto';
import {ShopOwnerDetailDto} from '../data/client/model/shopOwnerDetailDto';
import {UpdateShopData} from '../shop-details/shop-details.component';

@Component({
  selector: 'admin-details-page',
  templateUrl: './admin-details-page.component.html',
  styleUrls: ['./admin-details-page.component.css']
})
export class AdminDetailsPageComponent implements OnInit {

  shopDetails: ShopAdminDto;
  shopId: string;

  shopOwnerDetails: ReplaySubject<ShopOwnerDetailDto> = new ReplaySubject<ShopOwnerDetailDto>();

  constructor(private adminService: AdminService,
              private route: ActivatedRoute,
              private notificationsService: NotificationsService) {
  }

  ngOnInit() {
    this.shopId = this.route.snapshot.paramMap.get('id');
    this.adminService.getShopWithId(this.shopId)
      .subscribe(response => {
          this.shopDetails = response;
          this.shopOwnerDetails.next(this.extractShopOwnerDetailDto(this.shopDetails));
        },
        error => {
          console.log('Error requesting shop details: ' + error.status + ', ' + error.message);
          this.notificationsService.error('Tut uns leid!', 'Es ist ein Fehler beim Laden der Details aufgetreten.');
        });
  }

  updateShop($event: UpdateShopData) {
    this.adminService.updateShop($event);
  }

  deleteShop() {
    this.adminService.deleteShop(this.shopId);
  }

  changeShopApproval() {
    this.adminService.changeShopApproval(this.shopId, !this.shopDetails.approved);
    this.shopDetails.approved = !this.shopDetails.approved;
  }

  extractShopOwnerDetailDto(shopAdminDto: ShopAdminDto): ShopOwnerDetailDto {
    return {
      addressSupplement: shopAdminDto.addressSupplement,
      city: shopAdminDto.city,
      contactTypes: shopAdminDto.contactTypes,
      details: shopAdminDto.details,
      name: shopAdminDto.name,
      ownerName: shopAdminDto.ownerName,
      slots: shopAdminDto.slots,
      street: shopAdminDto.street,
      website: shopAdminDto.website,
      zipCode: shopAdminDto.zipCode
    };
  }

}
