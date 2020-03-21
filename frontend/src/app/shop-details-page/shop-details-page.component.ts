import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {
  BookingDialogData,
  BookingPopupComponent,
  BookingPopupOutcome,
  BookingPopupResult
} from '../booking-popup/booking-popup.component';
import {ShopControllerService} from "../data/client";
import {
  BookingSuccessData,
  BookingSuccessPopupComponent
} from "../booking-success-popup/booking-success-popup.component";

class ShopDetails {
  id: string;
  name: string;
  description: string;
  email: string;
  street: string;
  zipcode: string;
  country: string;
  website: string;
  slots: Slot[];
  supportedContactTypes: string[];
}

class Slot {
  id: string;
  start: string;
  end: string;
  available: boolean;
}

@Component({
  selector: 'shop-details-page',
  templateUrl: './shop-details-page.component.html',
  styleUrls: ['./shop-details-page.component.css']
})
export class ShopDetailsPageComponent implements OnInit {
  shopId: string;
  details: ShopDetails;


  constructor(private shopControllerService: ShopControllerService,
              private activatedRoute: ActivatedRoute,
              private matDialog: MatDialog) {
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap
      .subscribe(value => this.shopId = value.get('id'));
    this.refresh();
  }

  refresh() {
    // Test data until the backend works.
    this.details = {
      id: 'moes',
      name: 'Moe\'s Whiskeyladen',
      description: 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut ' +
        'labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea ' +
        'rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit ' +
        'amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam ' +
        'erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no ' +
        'sea takimata sanctus est Lorem ipsum dolor sit amet.',
      email: 'info@moesdrinks.com',
      street: 'Aschauer Straße 32',
      zipcode: '81549 München',
      country: 'Deutschland',
      website: 'https://www.moesdrinks.com',
      slots: [
        {
          id: '0',
          start: '10:00',
          end: '11:00',
          available: false
        },
        {
          id: '1',
          start: '11:00',
          end: '12:00',
          available: false
        },
        {
          id: '2',
          start: '12:00',
          end: '13:00',
          available: false
        },
        {
          id: '3',
          start: '13:00',
          end: '14:00',
          available: true
        },
        {
          id: '4',
          start: '14:00',
          end: '15:00',
          available: true
        },
        {
          id: '5',
          start: '15:00',
          end: '16:00',
          available: true
        }
      ],
      supportedContactTypes: ['WHATSAPP', 'PHONE']
    } as ShopDetails;
    this.shopControllerService.getDetailsUsingGET(this.shopId)
      .subscribe((shopDetails: ShopDetails) => {
        this.details = shopDetails;
      });
  }

  showBookingPopup(id: string) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.width = '100%';
    dialogConfig.data = {
      supportedContactTypes: this.details.supportedContactTypes
    } as BookingDialogData;
    this.matDialog.open(BookingPopupComponent, dialogConfig)
      .afterClosed()
      .subscribe((data: BookingPopupResult) => {
        if (data.outcome === BookingPopupOutcome.BOOK) {

          console.warn('NOT IMPLEMENTED', data, id);
          const registerSlotDto = {
            shop: this.shopId,
            slotId: id,
            contactType: data.option,
            contactNumber: data.phoneNumber,
            customerName: data.name,
            email: data.email
          };
          // TODO http call to the frontend api goes here

          const successConfig = new MatDialogConfig();
          successConfig.autoFocus = true;
          successConfig.width = '100%';
          successConfig.data = {
            owner: this.details.name,
            contactNumber: data.phoneNumber,
            contactType: data.option,
            start: this.details.slots.find(s => s.id === id).start,
            end: this.details.slots.find(s => s.id === id).end,
          } as BookingSuccessData;
          this.shopControllerService.getDetailsUsingGET(this.shopId)
            .toPromise()
            .then(value => this.matDialog.open(BookingSuccessPopupComponent, successConfig),
                value => this.matDialog.open(BookingSuccessPopupComponent, successConfig));
        }
      });
  }


}
