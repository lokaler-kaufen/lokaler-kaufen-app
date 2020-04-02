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
import {ContactTypesEnum} from '../contact-types/available-contact-types';

export interface SlotsPerDay {
  dayName: string;
  hasSlots: boolean;
  slots: Array<SlotDto>;
}

@Component({
  selector: 'shop-details-page',
  templateUrl: './shop-details-page.component.html',
  styleUrls: ['./shop-details-page.component.css']
})
export class ShopDetailsPageComponent implements OnInit {

  constructor(private client: HttpClient,
              private activatedRoute: ActivatedRoute,
              private matDialog: MatDialog,
              private notificationsService: NotificationsService) {
  }

  get hasDescription(): boolean {
    return this.shop?.details?.trim().length > 0;
  }

  shopId: string;
  shop: ShopDetailDto;
  activatedSlot: SlotsPerDay;

  slotsPerDay: Array<SlotsPerDay> = [];

  today: Date;

  weekday = ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag'];

  ngOnInit(): void {
    this.today = new Date();
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
        this.notificationsService.error('Tut uns leid!', 'Es ist ein Fehler beim Laden der Details aufgetreten.');
      });

    this.client.get<SlotsDto>('/api/reservation/' + this.shopId + '/slot?days=7')
      .subscribe((slots: SlotsDto) => {
        console.log('Subscribe Slots');
        // convert to map
        Object.keys(slots.slots).forEach(key => {
          this.slotsPerDay.push({
            dayName: this.getDayName(key),
            slots: slots.slots[key],
            hasSlots: slots.slots[key].length > 0
          });
          this.activatedSlot = this.slotsPerDay[0];
        });
      }, error => {
        console.log('Error requesting slots: ' + error.status + ', ' + error.message);
        this.notificationsService.error('Tut uns leid!', 'Es ist ein Fehler beim Laden der Slots aufgetreten.');
      });
  }

  showBookingPopup(slotId: string) {
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
            day: this.activatedSlot.dayName,
            start: this.activatedSlot.slots.find(s => s.id === slotId).start,
            end: this.activatedSlot.slots.find(s => s.id === slotId).end
          } as BookingSuccessData;
          const reservationDto: CreateReservationDto = {
            contact: data.phoneNumber,
            contactType: data.option,
            email: data.email,
            name: data.name,
            slotId
          };

          this.client.post<SlotsDto>('/api/reservation/' + this.shopId, reservationDto)
            .subscribe(() => {
                this.matDialog.open(BookingSuccessPopupComponent, successConfig);
                this.activatedSlot.slots.find(s => s.id === slotId).available = false;
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

  // monday is index 0, add slot offset % 7 to get day name
  getDayName(slotOffset): string {
    return this.weekday[(this.today.getDay() + parseInt(slotOffset, 10)) % 7];
  }

  public back(): void {
    window.history.back();
  }
}
