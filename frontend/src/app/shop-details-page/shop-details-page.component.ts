import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {
  BookingDialogData,
  BookingPopupComponent,
  BookingPopupOutcome,
  BookingPopupResult
} from '../booking-popup/booking-popup.component';
import {ShopDetailDto} from "../data/client";
import {BookingSuccessData} from "../booking-success-popup/booking-success-popup.component";
import {HttpClient} from "@angular/common/http";

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
  details: ShopDetailDto;


  constructor(private client: HttpClient,
              private activatedRoute: ActivatedRoute,
              private matDialog: MatDialog) {
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
        this.details = shopDetails;
      });
  }

  showBookingPopup(id: string) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.width = '100%';
    dialogConfig.data = {
      supportedContactTypes: this.details.contactTypes
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
            // start: this.details.slots.find(s => s.id === id).start,
            // end: this.details.slots.find(s => s.id === id).end,
          } as BookingSuccessData;

          /*this.shopControllerService.getDetailsUsingGET(this.shopId)
            .toPromise()
            .then(value => this.matDialog.open(BookingSuccessPopupComponent, successConfig),
              value => this.matDialog.open(BookingSuccessPopupComponent, successConfig));*/
        }
      });
  }


}
