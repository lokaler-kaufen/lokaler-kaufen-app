import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ContactTypes} from "../shared/contact-types";

export class BookingSuccessData {
  owner: string;
  contactNumber: string;
  contactType: string;
  start: string;
  end: string;
}

@Component({
  selector: 'booking-popup',
  templateUrl: './booking-success-popup.component.html',
  styleUrls: ['./booking-success-popup.component.css']
})
export class BookingSuccessPopupComponent implements OnInit {
  contactTypes = ContactTypes;
  constructor(public dialogRef: MatDialogRef<BookingSuccessPopupComponent>,
              @Inject(MAT_DIALOG_DATA) public data: BookingSuccessData) {
  }

  ngOnInit(): void {
  }

  onClick(): void {
    this.dialogRef.close();
  }

}
