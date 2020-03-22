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

  slots: Slot[] = [
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
  ];


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
          const successConfig = new MatDialogConfig();
          successConfig.autoFocus = true;
          successConfig.width = '100%';
          successConfig.data = {
            owner: this.details.name,
            contactNumber: data.phoneNumber,
            contactType: data.option,
            start: this.slots.find(s => s.id === id).start,
            end: this.slots.find(s => s.id === id).end,
          } as BookingSuccessData;

          /*this.shopControllerService.getDetailsUsingGET(this.shopId)
            .toPromise()
            .then(value => this.matDialog.open(BookingSuccessPopupComponent, successConfig),
              value => this.matDialog.open(BookingSuccessPopupComponent, successConfig));*/
        }
      });
  }


}
