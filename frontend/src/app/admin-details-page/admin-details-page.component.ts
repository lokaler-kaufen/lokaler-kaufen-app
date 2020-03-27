import {Component, OnInit} from '@angular/core';
import {ReplaySubject} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {AdminService} from '../shared/admin.service';
import {NotificationsService} from 'angular2-notifications';
import {ShopAdminDto} from '../data/client/model/shopAdminDto';
import {ShopOwnerDetailDto} from '../data/client/model/shopOwnerDetailDto';

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

  updateShop($event: ShopOwnerDetailDto) {
    this.injectUpdatedShopOwnerDetails($event);
    this.adminService.updateShop(this.shopDetails);
  }

  deleteShop() {
    this.adminService.deleteShop(this.shopId);
  }

  changeShopEnable() {
    this.adminService.changeShopEnable(this.shopId, !this.shopDetails.enabled);
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

  injectUpdatedShopOwnerDetails(shopOwnerDetails: ShopOwnerDetailDto) {
    this.shopDetails.addressSupplement = shopOwnerDetails.addressSupplement;
    this.shopDetails.city = shopOwnerDetails.city;
    this.shopDetails.contactTypes = shopOwnerDetails.contactTypes;
    this.shopDetails.details = shopOwnerDetails.details;
    this.shopDetails.name = shopOwnerDetails.name;
    this.shopDetails.ownerName = shopOwnerDetails.ownerName;
    this.shopDetails.slots = shopOwnerDetails.slots;
    this.shopDetails.street = shopOwnerDetails.street;
    this.shopDetails.website = shopOwnerDetails.website;
    this.shopDetails.zipCode = shopOwnerDetails.zipCode;
  }

}