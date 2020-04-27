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
import {ReservationClient} from '../api/reservation/reservation.client';

@Component({
  selector: 'shop-details-page',
  templateUrl: './shop-details-page.component.html',
  styleUrls: ['./shop-details-page.component.css']
})
export class ShopDetailsPageComponent implements OnInit {

  constructor(
    private client: HttpClient,
    private activatedRoute: ActivatedRoute,
    private matDialog: MatDialog,
    private notification: AsyncNotificationService,
    private reservation: ReservationClient
  ) {
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
  slotsData: ReplaySubject<ReserveSlotsData> = new ReplaySubject<ReserveSlotsData>();

  private static getReservationDto(data: BookingPopupResult, $event: SlotSelectionData) {
    const reservationDto: CreateReservationDto = {
      contact: data.phoneNumber,
      contactType: data.option,
      email: data.email,
      name: data.name,
      slotId: $event.slot.id
    };
    return reservationDto;
  }

  static createDialogConfig<T>(data: T): MatDialogConfig<T> {
    const cfg = new MatDialogConfig();
    cfg.autoFocus = true;
    cfg.width = '450px';
    cfg.data = data;
    return cfg;
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap
      .subscribe(value => {
        this.shopId = value.get('id');
        this.loadShopData();
        this.loadSlots();
      });
  }

  private loadShopData() {
    this.client.get<ShopDetailDto>('/api/shop/' + this.shopId).toPromise()
      .then((shopDetails: ShopDetailDto) => {
        this.shop = shopDetails;
      })

      .catch(error => {
        console.log('Error requesting shop details: ' + error.status + ', ' + error.message);
        this.notification.error('shop.details.error.generic.title', 'shop.details.error.generic.message');
      });
  }

  private loadSlots() {
    this.slotsData.next({slots: {days: []}});

    this.reservation.getSlotsForShop(this.shopId)
      .then((slots: SlotsDto) => {
        this.slotsData.next({slots});
      })

      .catch(error => {
        console.log('Error requesting slots: ' + error.status + ', ' + error.message);
        this.notification.error('shop.details.error.slots.title', 'shop.details.error.slots.message');
      });
  }

  showBookingPopup($event: SlotSelectionData) {
    const dialogConfig: MatDialogConfig<BookingDialogData> = ShopDetailsPageComponent.createDialogConfig({
      supportedContactTypes: this.shop.contactTypes
    });

    this.matDialog.open(BookingPopupComponent, dialogConfig)
      .afterClosed()
      .subscribe((data: BookingPopupResult) => {
        if (data && data.outcome === BookingPopupOutcome.BOOK) {
          const reservationDto = ShopDetailsPageComponent.getReservationDto(data, $event);

          this.reservation.createReservation(this.shopId, reservationDto)
            .then(() => {
              const successConfig: MatDialogConfig<BookingSuccessData> = ShopDetailsPageComponent.createDialogConfig({
                owner: this.shop.name,
                contactNumber: data.phoneNumber,
                contactType: ContactTypesEnum.getDisplayName(data.option),
                day: $event.slot.id,
                start: $event.slot.start,
                end: $event.slot.end
              });

              this.matDialog.open(BookingSuccessPopupComponent, successConfig);
            })

            .catch(error => {
              console.log('Error booking time slot: ' + error.status + ', ' + error.message);
              this.notification.error('shop.details.error.booking.title', 'shop.details.error.booking.message');
            })

            .finally(() => this.loadSlots());
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
