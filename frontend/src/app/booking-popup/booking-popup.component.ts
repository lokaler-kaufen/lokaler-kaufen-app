import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface BookingDialogData {}

export interface BookingPopupResult {
  outcome : BookingPopupOutcome;
  option : string;
  phoneNumber : string;
  name : string;
  email : string;
}

export enum BookingPopupOutcome {
  BOOKED, CANCELLED
}

@Component({
  selector: 'booking-popup',
  templateUrl: './booking-popup.component.html',
  styleUrls: ['./booking-popup.component.css']
})
export class BookingPopupComponent implements OnInit {

  options : string[] = [
    "WhatsApp",
    "Facetime"
  ];

  constructor(public dialogRef: MatDialogRef<BookingPopupComponent>,
              @Inject(MAT_DIALOG_DATA) public data: BookingDialogData) { }

  ngOnInit(): void {
  }

  onBook(): void {
    console.log('book it!');
    this.dialogRef.close();
  }

  onNoClick(): void {
    console.log('onNoClick');
    this.dialogRef.close();
  }

}
