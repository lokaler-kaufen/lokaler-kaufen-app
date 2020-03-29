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
import {NotificationsService} from 'angular2-notifications';
import {CreateReservationDto, ShopDetailDto, SlotDto, SlotsDto} from '../data/api';
import {ZipCodeCacheService} from '../landing-page/zip-code-cache.service';

@Component({
  selector: 'shop-details-page',
  templateUrl: './shop-details-page.component.html',
  styleUrls: ['./shop-details-page.component.css']
})
export class ShopDetailsPageComponent implements OnInit {

  shopId: string;
  shop: ShopDetailDto;
  slots: SlotDto[] = [];

  constructor(private client: HttpClient,
              private activatedRoute: ActivatedRoute,
              private matDialog: MatDialog,
              private notificationsService: NotificationsService,
              private zipCodeCacheService: ZipCodeCacheService) {
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap
      .subscribe(value => {
        this.shopId = value.get('id');
        this.refresh();
      });
  }

  refresh() {
    this.client.get<ShopDetailDto>('/api/shop/' + this.shopId)
      .subscribe((shopDetails: ShopDetailDto) => {
          this.shop = shopDetails;
        },
        error => {
          console.log('Error requesting shop details: ' + error.status + ', ' + error.message);
          this.notificationsService.error('Tut uns leid!', 'Es ist ein Fehler beim Laden der Details aufgetreten.');
        });

    this.client.get<SlotsDto>('/api/reservation/' + this.shopId + '/slot')
      .subscribe((slots: SlotsDto) => {
          this.slots = slots.slots;
        },
        error => {
          console.log('Error requesting slots: ' + error.status + ', ' + error.message);
          this.notificationsService.error('Tut uns leid!', 'Es ist ein Fehler beim Laden der Slots aufgetreten.');
        });
  }

  get hasDescription(): boolean {
    return this.shop?.details?.trim().length > 0;
  }

  get hasSlots(): boolean {
    return this.slots?.length > 0;
  }

  showBookingPopup(id: string) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.width = '450px';
    dialogConfig.data = {
      supportedContactTypes: this.shop.contactTypes
    } as BookingDialogData;
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
            contactType: data.option,
            start: this.slots.find(s => s.id === id).start,
            end: this.slots.find(s => s.id === id).end
          } as BookingSuccessData;
          const reservationDto: CreateReservationDto = {
            contact: data.phoneNumber,
            contactType: data.option.toUpperCase(),
            email: data.email,
            name: data.name,
            slotId: id
          };
          this.client.post<SlotsDto>('/api/reservation/' + this.shopId, reservationDto)
            .subscribe(() => {
                this.matDialog.open(BookingSuccessPopupComponent, successConfig);
                this.client.get<SlotsDto>('/api/reservation/' + this.shopId + '/slot')
                  .subscribe((slots: SlotsDto) => {
                    this.slots = slots.slots;
                  });
              },
              error => {
                console.log('Error booking time slot: ' + error.status + ', ' + error.message);
                this.notificationsService.error('Tut uns leid!', 'Es ist ein Fehler bei der Buchung aufgetreten.');
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

  getCachedZipCode() {
    return this.zipCodeCacheService.getZipCode();
  }

}
