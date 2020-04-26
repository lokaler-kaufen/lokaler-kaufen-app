import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {
  BookingDialogData,
  BookingPopupComponent,
  BookingPopupOutcome,
  BookingPopupResult
} from '../booking-popup/booking-popup.component';
import {
  BookingSuccessData,
  BookingSuccessPopupComponent
} from '../booking-success-popup/booking-success-popup.component';
import {HttpClient} from '@angular/common/http';
import {CreateReservationDto, ShopDetailDto, SlotsDto} from '../data/api';
import {ContactTypesEnum} from '../contact-types/available-contact-types';
import {AsyncNotificationService} from '../i18n/async-notification.service';
import {ReplaySubject} from 'rxjs';
import {ReserveSlotsData, SlotSelectionData} from '../slots/slots.component';

@Component({
  selector: 'shop-details-page',
  templateUrl: './shop-details-page.component.html',
  styleUrls: ['./shop-details-page.component.css']
})
export class ShopDetailsPageComponent implements OnInit {

  constructor(private client: HttpClient,
              private activatedRoute: ActivatedRoute,
              private matDialog: MatDialog,
              private notificationsService: AsyncNotificationService) {
  }

  get hasDescription(): boolean {
    return this.shop?.details?.trim().length > 0;
  }

  get hasOnlyPhone(): boolean {
    return (this.shop?.contactTypes?.length === 1 &&
      this.shop?.contactTypes?.values()?.next()?.value === 'PHONE');
  }

  get logoBackgroundColor(): string {
    return this.shop.autoColorEnabled ? this.shop.shopColor : '#FFFFFF';
  }

  shopId: string;
  shop: ShopDetailDto;

  slotsPerDay: ReplaySubject<ReserveSlotsData> = new ReplaySubject<ReserveSlotsData>();

  slotsConfig: SlotsDto;

  ngOnInit(): void {
    this.activatedRoute.paramMap
      .subscribe(value => {
        console.log('subscribe param map');
        this.shopId = value.get('id');
        this.refresh();
      });
  }

  refresh() {
    this.client.get<ShopDetailDto>('/api/shop/' + this.shopId)
      .toPromise().then((shopDetails: ShopDetailDto) => {
      this.shop = shopDetails;
    })
      .catch(error => {
        console.log('Error requesting shop details: ' + error.status + ', ' + error.message);
        this.notificationsService.error('shop.details.error.generic.title', 'shop.details.error.generic.message');
      });

    this.client.get<SlotsDto>('/api/reservation/' + this.shopId + '/slot?days=7')
      .subscribe((slots: SlotsDto) => {
        this.slotsConfig = slots;
        this.slotsPerDay.next({slots});
      }, error => {
        console.log('Error requesting slots: ' + error.status + ', ' + error.message);
        this.notificationsService.error('shop.details.error.slots.title', 'shop.details.error.slots.message');
      });
  }

  showBookingPopup($event: SlotSelectionData) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.width = '450px';
    dialogConfig.data = {supportedContactTypes: this.shop.contactTypes} as BookingDialogData;

    this.matDialog.open(BookingPopupComponent, dialogConfig)
      .afterClosed()
      .subscribe((data: BookingPopupResult) => {
        if (data && data.outcome === BookingPopupOutcome.BOOK) {
          const successConfig = new MatDialogConfig();
          successConfig.autoFocus = true;
          successConfig.width = '450px';
          successConfig.data = {
            owner: this.shop.name,
            contactNumber: data.phoneNumber,
            contactType: ContactTypesEnum.getDisplayName(data.option),
            day: $event.slot.id,
            start: $event.slot.start,
            end: $event.slot.end
          } as BookingSuccessData;
          const reservationDto: CreateReservationDto = {
            contact: data.phoneNumber,
            contactType: data.option,
            email: data.email,
            name: data.name,
            slotId: $event.slot.id
          };

          this.client.post<SlotsDto>('/api/reservation/' + this.shopId, reservationDto)
            .subscribe(() => {
                this.matDialog.open(BookingSuccessPopupComponent, successConfig);
              },
              error => {
                console.log('Error booking time slot: ' + error.status + ', ' + error.message);
                this.notificationsService.error('shop.details.error.booking.title', 'shop.details.error.booking.message');
              });
        }
      });
  }

  returnValidLink(url: string) {
    if (!url) {
      return '';
    }
    let result;
    const startingUrl = 'http://';
    const httpsStartingUrl = 'https://';
    if (url.startsWith(startingUrl) || url.startsWith(httpsStartingUrl)) {
      result = url;
    } else {
      result = startingUrl + url;
    }
    return result;
  }

  back(): void {
    window.history.back();
  }

  returnValidFacebookLink(handle: string) {
    return `https://www.facebook.com/${handle}/`;
  }

  returnValidInstagramLink(handle: string) {
    return `https://www.instagram.com/${handle}/`;
  }

  returnValidTwitterLink(handle: string) {
    return `https://www.twitter.com/${handle}/`;
  }
}
