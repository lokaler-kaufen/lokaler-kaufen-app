import {Component, OnInit} from '@angular/core';
import {ReplaySubject} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {AdminService} from '../service/admin.service';
import {NotificationsService} from 'angular2-notifications';
import {UpdateShopData} from '../shop-details-config/shop-details-config.component';
import {ShopAdminDto, ShopOwnerDetailDto} from '../data/api';
import {AsyncNotificationService} from '../i18n/async-notification.service';

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

  constructor(
    private adminService: AdminService,
    private route: ActivatedRoute,
    private notificationsService: NotificationsService,
    private asyncNS: AsyncNotificationService
  ) {
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
        this.asyncNS.error('admin.details.shopDetailsError.title', 'admin.details.shopDetailsError.content');
      });
  }

  updateShop($event: UpdateShopData) {
    this.adminService.updateShop($event)
      .then(() => {
        this.asyncNS.success(
          'admin.details.shopUpdateSuccess.title',
          'admin.details.shopUpdateSuccess.content'
        );
      })
      .catch(error => {
        console.log('Error updating shop: ' + error.status + ', ' + error.message);
        this.asyncNS.error('admin.details.shopUpdateError.title', 'admin.details.shopUpdateError.content');
      });
  }

  deleteShop() {
    this.adminService.deleteShop(this.shopId)
      .then(() => {
        this.asyncNS.success(
          'admin.details.shopDeleteSuccess.title',
          'admin.details.shopDeleteSuccess.content'
        );
      })
      .catch(error => {
        console.log('Error updating shop: ' + error.status + ', ' + error.message);
        this.asyncNS.error('admin.details.shopDeleteError.title', 'admin.details.shopDeleteError.content');
      });
  }

  changeShopApproval() {
    const nextValue = !this.shopDetails.approved;

    this.adminService.changeShopApproval(this.shopId, nextValue)
      .then(() => {
        this.asyncNS.success(
          'admin.details.shopApprovalSuccess.title',
          {key: 'admin.details.shopApprovalSuccess.content', params: {nextValue}}
        );
      })
      .catch(error => {
        console.log('Error updating shop: ' + error.status + ', ' + error.message);
        this.asyncNS.error('admin.details.shopApprovalError.title', 'admin.details.shopApprovalError.content');
      })
      .finally(() => this.shopDetails.approved = nextValue);
  }

  extractShopOwnerDetailDto(shopAdminDto: ShopAdminDto): ShopOwnerDetailDto {
    return shopAdminDto as ShopOwnerDetailDto;
  }

}
